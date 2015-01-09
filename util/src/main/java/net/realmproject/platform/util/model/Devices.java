package net.realmproject.platform.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.model.schema.Assignment;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Session;

public class Devices {

	
	public static Session getActiveSession(Transaction tx, Device device) {
		
		//list of all sessions containing this device
		Iterable<Session> sessions = getSessions(tx, device);
		
		//A device should only have one active session at a time.
		for (Session session : sessions) {
			if (Sessions.isLive(session)) {
				return session;
			}
		}
		
		return null;
	}
	

	public static Iterable<Session> getSessions(Transaction tx, Device device) {
		return tx.query("Session", new IQuery("devices", Relation.CONTAINS, device));
	}
	
	public static Iterable<Assignment> getAssignments(Transaction tx, Device device) {
		return tx.query("Assignment", new IQuery("devices", Relation.CONTAINS, device));
	}
	
}
