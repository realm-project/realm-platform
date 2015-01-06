package net.realmproject.util.model;

import java.util.stream.Collectors;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Session;
import net.realmproject.util.RealmLog;
import net.realmproject.util.RealmRepo;

import org.apache.commons.logging.Log;


public class Persons {

	protected static Log log = RealmLog.getLog();
	
	public static Person fromUsername(Transaction tx, String username) {
		if (username == null) {
			log.debug("username was null");
			return null;
		}
		return RealmRepo.queryHead(tx, "Person", new IQuery("email", username));
	}
	
	public static Iterable<Session> getActiveSessions(Person person) {
		return person.getSessions().stream().filter(Sessions::isLive).collect(Collectors.toList());
	}
	
	public static Iterable<Device> getOwnedDevices(Transaction tx, Person owner) {
		return tx.query("Device", new IQuery("owner", owner));
	}
	

	
}
