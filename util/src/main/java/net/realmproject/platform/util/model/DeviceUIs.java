package net.realmproject.platform.util.model;


import net.objectof.model.Transaction;
import net.objectof.model.query.IMultiQuery;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceUI;


public class DeviceUIs {

    public static Iterable<Assignment> getAssignments(Transaction tx, DeviceUI deviceUI) {
        return tx.query("Assignment", new IMultiQuery("devices", getDevices(tx, deviceUI)));
    }

    public static Iterable<Device> getDevices(Transaction tx, DeviceUI deviceUI) {
        return tx.query("Device", new IQuery("deviceUIs", Relation.CONTAINS, deviceUI));
    }

}
