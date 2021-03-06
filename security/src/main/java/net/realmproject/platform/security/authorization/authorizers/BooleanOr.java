package net.realmproject.platform.security.authorization.authorizers;


import java.util.ArrayList;
import java.util.List;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.authorization.Authorizer;


public class BooleanOr extends AbstractAuthorizer {

    private List<Authorizer> auths;

    public BooleanOr(Authorizer a1) {
        auths = new ArrayList<>();
        auths.add(a1);
    }

    public BooleanOr(Authorizer a1, Authorizer a2) {
        auths = new ArrayList<>();
        auths.add(a1);
        auths.add(a2);
    }

    public BooleanOr(Authorizer a1, Authorizer a2, Authorizer a3) {
        auths = new ArrayList<>();
        auths.add(a1);
        auths.add(a2);
        auths.add(a3);
    }

    public BooleanOr(List<Authorizer> auths) {
        this.auths = auths;
    }

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {
        for (Authorizer auth : auths) {
            if (auth.authorize(action, request, person)) return true;
        }
        return false;
    }

    public boolean cacheable() {
        return auths.stream().map(a -> a.cacheable()).reduce(true, (a, b) -> a && b);
    }

}
