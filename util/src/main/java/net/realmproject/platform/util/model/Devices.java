package net.realmproject.platform.util.model;


import java.util.List;
import java.util.ArrayList;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.schema.Station;


public class Devices {

    public static Session getActiveSession(Transaction tx, Device device) {

        // list of all sessions containing this device
        Iterable<Session> sessions = getSessions(tx, device);

        // A device should only have one active session at a time.
        for (Session session : sessions) {
            if (Sessions.isLive(session)) { return session; }
        }

        return null;
    }

    public static Iterable<Station> getStations(Transaction tx, Device device) {
        return tx.query("Station", new IQuery("devices", Relation.CONTAINS, device));
    }
    
    public static Iterable<Session> getSessions(Transaction tx, Device device) {
    	List<Session> sessions = new ArrayList<Session>();
    	    	
    	Iterable<Station> stations = getStations(tx, device);
    	
    	for (Station station : stations) {
    		Iterable<Session> tempSessions = tx.query("Session", new IQuery("Station", Relation.EQUAL, station));
    		for (Session s : tempSessions)
    			sessions.add(s);   	
    	}
    	
    	return sessions;
    }

    public static Iterable<Assignment> getAssignments(Transaction tx, Device device) {
        return tx.query("Assignment", new IQuery("devices", Relation.CONTAINS, device));
    }

}
