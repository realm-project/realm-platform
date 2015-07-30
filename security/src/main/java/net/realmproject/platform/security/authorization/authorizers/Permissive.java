package net.realmproject.platform.security.authorization.authorizers;


import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;


public class Permissive extends AbstractAuthorizer {

    public Permissive() {}

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {
        return true;
    }

}
