package net.realmproject.platform.corc.accessor;


import java.util.Date;
import java.util.concurrent.TimeUnit;

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
import net.realmproject.dcm.features.Statefulness.State;
import net.realmproject.dcm.features.command.Command;
import net.realmproject.dcm.features.command.CommandState;
import net.realmproject.dcm.features.recording.RecordWriter;
import net.realmproject.dcm.util.DCMThreadPool;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.DeviceIO;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.RealmSerialize;
import net.realmproject.platform.util.model.DeviceCommands;
import net.realmproject.platform.util.model.Devices;


// Race conditions in sending a new command vs receiving an idle state or a
// state for an old command make in impractical to just record a single command
// at a time.
public class CommandRecordWriter implements RecordWriter<DeviceEvent>, Logging {

    Connector connector;
    Package pkg;
    Transaction tx;

    Id<Composite> deviceId;
    boolean isDirty = false;
    Id<Composite> lastCommand;
    boolean isClosed = false;

    public CommandRecordWriter(String deviceId, Connector connector) throws ConnectorException {
        this.connector = connector;
        pkg = connector.getPackage();
        tx = pkg.connect(CommandRecordWriter.class.getName());

        // look up this device's id
        Device device = RealmRepo.queryHead(tx, "Device", new IQuery("name", deviceId));
        this.deviceId = device.id();

        DCMThreadPool.getPool().scheduleAtFixedRate(this::flush, 1, 1, TimeUnit.MINUTES);

    }

    @Override
    public synchronized void close() {
        tx.post();
        isClosed = true;
    }

    @Override
    public synchronized void write(DeviceEvent event) {
        if (isClosed) { throw new IllegalStateException("Resource is closed"); }

        Object payload = event.getValue();
        DeviceEventType eventType = event.getDeviceEventType();

        if (eventType == DeviceEventType.MESSAGE && payload instanceof Command) {
            // for commands
            onCommand((Command) payload);

        } else if (eventType == DeviceEventType.VALUE_CHANGED && payload instanceof CommandState) {
            // for responses to commands
            onState((CommandState) payload);
        }

    }

    @Override
    public synchronized void flush() {
        if (isClosed) { throw new IllegalStateException("Resource is closed"); }
        if (!isDirty) { return; }
        tx.post();
        tx = pkg.connect(CommandRecordWriter.class.getName());
        isDirty = false;
    }

    private synchronized void onState(CommandState state) {

        // only record output from the arm if the state is "busy"
        if (state.mode != null) {
            boolean isBusy = state.mode == State.Mode.BUSY;
            if (!isBusy) {
                // if the state is not busy, try flushing (if dirty)
                flush();
                return;
            }
        }

        // Find the command with a command id equal to this state's command id
        DeviceCommand lastCommand = DeviceCommands.forId(tx, state.commandId());
        if (lastCommand == null) { return; }

        // the states of the last command should be already created
        IIndexed<DeviceIO> states = (IIndexed<DeviceIO>) lastCommand.getStates();
        if (states == null) { return; }

        String json = RealmSerialize.serialize(state);
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
        if (!command.record) { return; }

        Transaction tx = pkg.connect(CommandRecordWriter.class.getName());

        // Extract json command
        String json = RealmSerialize.serialize(command);

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

        // Create the list of commands for the retrieved session, if not created
        // already
        if (session.getCommands() == null) {
            session.setCommands(tx.create("Session.commands"));
        }

        // Add the newly created DeviceCommand (dc) to the list of commands of
        // the session
        IIndexed<DeviceCommand> commands = (IIndexed<DeviceCommand>) session.getCommands();
        commands.add(dc);

        getLog().info("Wrote DeviceCommand: " + dc);
        getLog().info("DeviceCommand ID: " + dc.id());
        getLog().info("DeviceCommand Label: " + dc.id().label());

        isDirty = true;

        tx.post();
        tx.close();

    }

    private long timestamp() {
        return new Date().getTime();
    }

}