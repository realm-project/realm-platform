package net.realmproject.platform.util.model;

import java.util.Calendar;
import java.util.Date;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Session;
import net.realmproject.platform.util.RealmMedia;
import net.realmproject.platform.util.RealmRepo;

public class Sessions {

	public static Iterable<Person> getStudents(Transaction tx, Session session) {
		return tx.query("Person", new IQuery("sessions", Relation.CONTAINS, session));
	}
	
	public static Session withToken(Transaction tx, String token) {
		return RealmRepo.queryHead(tx, "Session", new IQuery("sessionToken", token));
	}
	
	public static String title(Session s) {
		return s.getSessionToken() + " for " + s.getAssignment().getCourse().getName() + " - " + s.getAssignment().getName();
	}
	
	public static String iCal(Session s) {	
		return RealmMedia.iCal("Session " + title(s), s.getStartTime(), s.getDuration().intValue());
	}
	
	
	public static boolean isLive(Session s) {
		
		Date start = s.getStartTime();
		Date end = endTime(s);
		Date now = new Date();
		
		if (start.after(now)) { return false; }	//if it hasn't started
		if (end.before(now)) { return false; }	//if it's already ended
		return true;
	
	}
	
	public static Date endTime(Session s) {
		return endTime(s.getStartTime(), s.getDuration());
	}
	
	private static Date endTime(Date start, long length) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Calendar.MINUTE, (int) length);
		return cal.getTime();
	}
	

	
}
