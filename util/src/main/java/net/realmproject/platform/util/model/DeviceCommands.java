package net.realmproject.platform.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmRepo;

public class DeviceCommands {

	public static Session getSession(Transaction tx, DeviceCommand command) {
		
		return RealmRepo.queryHead(tx, "Session", new IQuery("commands", Relation.CONTAINS, command));
	}
}
