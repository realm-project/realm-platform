package net.realmproject.platform.corc.accessor;


import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import net.objectof.aggr.Composite;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.model.Id;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.query.IQuery;
import net.realmproject.dcm.event.DeviceEvent;
import net.realmproject.dcm.event.DeviceEventType;
import net.realmproject.dcm.event.Logging;
import net.realmproject.dcm.features.command.Command;
import net.realmproject.dcm.features.recording.RecordWriter;
import net.realmproject.dcm.features.stateful.State;
import net.realmproject.dcm.util.DCMInterrupt;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.dcm.util.DCMThreadPool;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.DeviceIO;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.model.DeviceCommands;
import net.realmproject.platform.util.model.Devices;


// Race conditions in sending a new command vs receiving an idle state or a
// state for an old command make in impractical to just record a single command
// at a time.
public class CommandRecordWriter implements RecordWriter<DeviceEvent>, Logging {

    public static final String COMMAND_ID = "CommandId";

    Connector connector;
    Package pkg;
    Transaction tx;

    private Id<Composite> deviceId;
    private boolean isDirty = false;
    private boolean isClosed = false;

    private boolean recordAllCommands = false;

    private BlockingQueue<DeviceEvent> eventQueue = new LinkedBlockingQueue<>();

    // caches DeviceCommand objects so we don't always have to query them.
    // Remember to clear the cache when the tx is posted/changed
    LoadingCache<String, DeviceCommand> deviceCommandCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            // flush will clear it anyways
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build(new CacheLoader<String, DeviceCommand>() {

                @Override
                public DeviceCommand load(String key) {
                    return DeviceCommands.forId(tx, key);
                }
            });

    // remember which Commands requested recording so that we don't have to
    // deserialize the DeviceCommand's DeviceIO json every time we want to
    // record state. Expire after a few minutes to prevent Commands which never
    // terminate from flooding the repo
    Cache<String, Boolean> recordCommandCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public CommandRecordWriter(String deviceId, Connector connector) throws ConnectorException {
        this.connector = connector;
        pkg = connector.getPackage();
        tx = pkg.connect(CommandRecordWriter.class.getName());

        // look up this device's id once at the start
        Device device = RealmRepo.queryHead(tx, "Device", new IQuery("name", deviceId));
        this.deviceId = device.id();

        // flush the transaction every minute
        DCMThreadPool.getScheduledPool().scheduleAtFixedRate(this::flush, 1, 1, TimeUnit.MINUTES);

        // write queued events into transaction
        DCMThreadPool.getPool().submit(() -> {
            DCMInterrupt.handle(this::writeQueuedEvents, e -> e.printStackTrace());
        });

    }

    @Override
    public synchronized void close() {
        tx.post();
        isClosed = true;
    }

    @Override
    public synchronized void write(DeviceEvent event) {
        if (isClosed) {
            getLog().info("Attempting to write to closed " + getClass().getSimpleName());
            return;
        }

        eventQueue.offer(event);
    }

    private void writeQueuedEvents() throws InterruptedException {

        while (true) {

            DeviceEvent event = eventQueue.take();
            Object payload = event.getPayload();
            DeviceEventType eventType = event.getDeviceEventType();

            if (eventType == DeviceEventType.MESSAGE && payload instanceof Command) {
                // for commands
                onCommand((Command) payload);

            } else if (eventType == DeviceEventType.VALUE_CHANGED && payload instanceof State) {
                // for responses to commands
                onState((State) payload);
            }
        }

    }

    @Override
    public synchronized void flush() {
        if (isClosed) { throw new IllegalStateException("Resource is closed"); }
        if (!isDirty) { return; }
        tx.post();
        deviceCommandCache.invalidateAll();
        tx = pkg.connect(CommandRecordWriter.class.getName());
        isDirty = false;
    }

    private synchronized void onState(State state) {

        // only record output from the arm if the state is "busy"
        if (state.mode != null) {
            boolean isBusy = state.mode == State.Mode.BUSY;
            if (!isBusy) { return; }
        }

        // Find the command with a command id equal to this state's command id
        String commandId = (String) state.getProperty(COMMAND_ID);
        if (commandId == null) { return; }

        // check if this command indicated that it wanted state to be recorded
        Boolean toRecord = recordCommandCache.getIfPresent(commandId);
        if (toRecord == null || toRecord != true) { return; }

        // find the DeviceCommand based on the command id
        DeviceCommand lastCommand = deviceCommandCache.getUnchecked(commandId);
        if (lastCommand == null) { return; }

        // the states of the last command should be already created
        IIndexed<DeviceIO> states = (IIndexed<DeviceIO>) lastCommand.getStates();
        if (states == null) { return; }

        String json = DCMSerialize.serialize(state);
        // don't add if it's the same as before
        if (states.size() > 0) {
            DeviceIO lastState = states.get(states.size() - 1);
            if (json.equals(lastState.getJson())) { return; }
        }

        // create a new deviceIO and add it to the states
        DeviceIO dio = tx.create("DeviceIO");
        dio.setUnixtime(timestamp());
        dio.setJson(json);

        // add it
        states.add(dio);

        // don't post here, instead we rely on a scheduled thread to post this
        // transaction regularly this keeps devices from spamming the device
        // recorder and the device recorder from spamming the database

        getLog().debug("DeviceRecorder: Adding state for command " + lastCommand.id());
        isDirty = true;

    }

    private synchronized void onCommand(Command command) {
        // if the command doesn't ask for recording, and we're not recording all
        // commands (not all responses), then just exit out
        if (!command.isToRecord() && !recordAllCommands) { return; }

        // save the command's desire for recording in the cache.
        recordCommandCache.put(command.getId(), command.isToRecord());

        // Extract json command
        String json = DCMSerialize.serialize(command);

        // Retrieve the device with deviceId mentioned in method arguments
        Device device = tx.retrieve(deviceId);
        if (device == null) {
            getLog().info("Unable to record command -- device was null");
            return;
        }

        // Retrieve the first active session that have the device in the list of
        // its devices
        Session session = Devices.getActiveSession(tx, device);
        if (session == null) {
            getLog().info("Unable to record command -- could not find active session");
            return;
        } // no active session

        // Create a DeviceIO and set its json with the json extracted from
        // command argument
        DeviceIO dio = tx.create("DeviceIO");
        dio.setUnixtime(timestamp());
        dio.setJson(json);

        // Create a new DeviceCommand, set its command to the newly created
        // DeviceIO, initialize its states list, and set its device to the
        // devcie in method arguments
        DeviceCommand dc = tx.create("DeviceCommand");
        dc.setCommand(dio);
        dc.setStates(tx.create("DeviceCommand.states"));
        dc.setDevice(device);
        dc.setUuid(command.getId());

        // Create the list of commands for the retrieved session, if not created
        // already
        if (session.getCommands() == null) {
            session.setCommands(tx.create("Session.commands"));
        }

        // Add the newly created DeviceCommand (dc) to the list of commands of
        // the session
        IIndexed<DeviceCommand> commands = (IIndexed<DeviceCommand>) session.getCommands();
        commands.add(dc);

        getLog().trace("Wrote DeviceCommand: " + dc);
        getLog().trace("DeviceCommand ID: " + dc.id());
        getLog().trace("DeviceCommand Label: " + dc.id().label());

        isDirty = true;

    }

    private long timestamp() {
        return new Date().getTime();
    }

    public boolean isRecordAllCommands() {
        return recordAllCommands;
    }

    public void setRecordAllCommands(boolean recordAllCommands) {
        this.recordAllCommands = recordAllCommands;
    }

}
