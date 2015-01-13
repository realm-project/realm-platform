package net.realmproject.platform.corc.accessor;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.objectof.aggr.Composite;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.model.Id;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.query.IQuery;
import net.realmproject.dcm.accessor.DeviceRecorder;
import net.realmproject.dcm.command.Command;
import net.realmproject.dcm.command.DeviceState;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.DeviceIO;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmLog;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.RealmSerialize;
import net.realmproject.platform.util.RealmThread;
import net.realmproject.platform.util.model.Devices;

import org.apache.commons.logging.Log;


public class IDeviceRecorder implements DeviceRecorder {

    final static int DEFAULT_COMMIT_INTERVAL = 1;

    int interval = DEFAULT_COMMIT_INTERVAL;
    String idString;
    Id<Composite> id;
    Package repo;
    private Transaction recordStateTx;
    private boolean modified = false;

    private Log log = RealmLog.getLog();

    public IDeviceRecorder(Connector connector, String idString) throws ConnectorException {
        this(connector, idString, DEFAULT_COMMIT_INTERVAL);
    }

    public IDeviceRecorder(Connector connector, String idString, int interval) throws ConnectorException {
        this.idString = idString;
        this.interval = interval;
        repo = connector.getPackage();
        recordStateTx = createStateTx();

        Device device = RealmRepo.queryHead(recordStateTx, "Device", new IQuery("name", idString));
        id = device.id();

        RealmThread.getThreadPool().scheduleAtFixedRate(this::commitStateTx, interval, interval, TimeUnit.SECONDS);
    }

    private Transaction createStateTx() {
        return repo.connect(getClass().getName() + " - " + id.getUniqueName());
    }

    private synchronized void commitStateTx() {
        log.debug("DeviceRecorder: Checking if commit is needed...");
        if (!modified) {
            log.debug("DeviceRecorder: No commit required");
            return;
        }
        log.debug("DeviceRecorder: Commit required, posting...");
        recordStateTx.post();
        recordStateTx.close();
        recordStateTx = createStateTx();
        modified = false;
        log.debug("DeviceRecorder: Commit completed");
    }

    @Override
    public synchronized String recordState(Serializable state) {

        // only record output from the arm if the state is "busy"
        DeviceState realmstate = RealmSerialize.convertMessage(state, DeviceState.class);
        if (realmstate.mode != null) {
            boolean isBusy = realmstate.mode == DeviceState.Mode.BUSY;
            if (!isBusy) { return null; }
        }

        Transaction tx = recordStateTx;

        // retrieve the device using the deviceId argument
        Device device = tx.retrieve(id);
        if (device == null) { return null; }

        // retrieve the active session for this device
        Session session = Devices.getActiveSession(tx, device);
        if (session == null) { return null; } // no active session

        // Find the last command with a device equal to this device
        DeviceCommand lastCommand = null;
        Boolean found = false;
        List<DeviceCommand> commands = session.getCommands();
        if (commands == null) { return null; } // It is an error!
        if (commands.size() == 0) { return null; } // It means that no command
                                                   // is
        // issued for this device, during
        // this session yet. No need to
        // record state

        int index = commands.size() - 1;
        for (int i = index; i >= 0 && !found; i--)
            if (commands.get(index).getDevice() != null && commands.get(index).getDevice().equals(device)) {
                lastCommand = commands.get(index);
                found = true;
            }

        if (lastCommand == null) { return null; } // No command with this device
        // exists in the command list of
        // this session! It is an error!

        // the states of the last command should be already created
        IIndexed<DeviceIO> states = (IIndexed<DeviceIO>) lastCommand.getStates();
        if (states == null) { return null; }

        String json = RealmSerialize.serialize(state);

        // don't add if it's the same as before
        if (states.size() > 0) {
            DeviceIO lastState = states.get(states.size() - 1);
            if (json.equals(lastState.getJson())) { return null; }
        }

        // create a new deviceIO and add it to the states
        DeviceIO dio = tx.create("DeviceIO");
        dio.setUnixtime(timestamp());
        dio.setJson(json);

        states.add(dio);

        // don't post here, instead we rely on a scheduled thread to post this
        // transaction regularly this keeps devices from spamming the device
        // recorder and the device recorder from spamming the database

        // tx.post();
        log.debug("DeviceRecorder: Adding state for command " + lastCommand.id());
        modified = true;

        return dio.id().label().toString();
    }

    @Override
    public synchronized String recordCommand(Command command) {

        Transaction tx = repo.connect(getClass().getName());

        // Extract json command
        String json = RealmSerialize.serialize(command);

        // Retrieve the device with deviceId mentioned in method arguments
        Device device = tx.retrieve(id);
        if (device == null) {
            log.info("Unable to record command -- device was null");
            return null;
        }

        // Retrieve the first active session that have the device in the list of
        // its devices
        Session session = Devices.getActiveSession(tx, device);
        if (session == null) {
            log.info("Unable to record command -- could not find active session");
            return null;
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

        tx.post();
        tx.close();

        log.info("Wrote DeviceCommand: " + dc);
        log.info("DeviceCommand ID: " + dc.id());
        log.info("DeviceCommand Label: " + dc.id().label());

        return dc.id().label().toString();
    }

    private long timestamp() {
        return new Date().getTime();
    }

}
