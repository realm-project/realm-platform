package net.realmproject.platform.util.model;


import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Role;


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
