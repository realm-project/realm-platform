package net.realmproject.platform.util.model;


import net.realmproject.platform.schema.Person;


public class Admins {

    public static boolean isAdmin(Person person) {
        return person.getRole().getName().equals("admin");
    }

}
