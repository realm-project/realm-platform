package net.realmproject.platform.security.authorization.authorizers;


import java.util.Collections;
import java.util.List;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;


public class Role extends AbstractAuthorizer {

    private List<String> roleNames;

    public Role(String roleName) {
        this(Collections.singletonList(roleName));
    }

    public Role(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {

        String personRole = person.getRole().getName();

        for (String roleName : roleNames) {
            if (roleName.equalsIgnoreCase(personRole)) { return true; }
        }

        return false;

    }

}
