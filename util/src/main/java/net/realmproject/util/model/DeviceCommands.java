package net.realmproject.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.model.schema.DeviceCommand;
import net.realmproject.model.schema.Session;
import net.realmproject.util.RealmRepo;

public class DeviceCommands {

	public static Session getSession(Transaction tx, DeviceCommand command) {
		
		return RealmRepo.queryHead(tx, "Session", new IQuery("commands", Relation.CONTAINS, command));
	}
}
