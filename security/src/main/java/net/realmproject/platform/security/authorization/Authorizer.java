package net.realmproject.platform.security.authorization;


import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;


public interface Authorizer {

    // TODO: Change this function to return null on auth fail, and a string
    // which uniquely identifies the authorization request (within the context
    // of the concrete authorizer) for authorization cacheing
    boolean authorize(Action action, HttpRequest request, Person person);

    String uuid();

    default boolean cacheable() {
        return false;
    }

    default String cacheString(Action action, HttpRequest request) {
        return "";
    }

}
