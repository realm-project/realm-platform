package net.realmproject.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Role;

public class Students {

	public static Iterable<Person> enumerate(Transaction tx) {
		return tx.query("Person", new IQuery("role", Students.role(tx)));		
	}
	
	public static Role role(Transaction tx) {
		Role role = Roles.get(tx, "student");
		if (role == null) throw new NullPointerException();
		return role;
	}
	
}
