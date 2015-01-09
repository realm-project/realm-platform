package net.realmproject.platform.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.model.schema.DeviceCommand;
import net.realmproject.model.schema.DeviceIO;
import net.realmproject.model.schema.Session;
import net.realmproject.platform.util.RealmRepo;

public class DeviceIOs {

	public static DeviceCommand getDeviceCommand(Transaction tx, DeviceIO io) {
		return RealmRepo.queryHead(tx, "DeviceCommand", new IQuery("states", Relation.CONTAINS, io));
	}
	
	public static Session getSession(Transaction tx, DeviceIO io) {
		DeviceCommand command = getDeviceCommand(tx, io);
		if (command == null) return null;
		return DeviceCommands.getSession(tx, command);
	}
}
