package net.realmproject.platform.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.model.schema.Assignment;
import net.realmproject.model.schema.Session;

public class Assignments {

	public static Iterable<Session> getSessions(Transaction tx, Assignment asn) {
		return tx.query("Session", new IQuery("assignment", asn));
	}
	
	public static Iterable<Assignment> forTemplate(Transaction tx, Assignment template) {
		return tx.query("Assignment", new IQuery("assignment", template));
	}
	
	
}
