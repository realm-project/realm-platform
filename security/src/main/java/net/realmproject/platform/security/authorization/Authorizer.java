package net.realmproject.platform.security.authorization;


import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;


public interface Authorizer {

    boolean authorize(Action action, HttpRequest request, Person person);

}
