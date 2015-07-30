package net.realmproject.platform.security.authorization.authorizers;


import java.util.UUID;

import net.realmproject.platform.security.authorization.Authorizer;


public abstract class AbstractAuthorizer implements Authorizer {

    private String uuid = UUID.randomUUID().toString();

    @Override
    public String uuid() {
        return uuid;
    }

}
