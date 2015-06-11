package net.realmproject.platform.security.authorization;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmResponse;


public class IAuthorizationHandler extends ISessionHandler {

    private Authorizer auth;

    public IAuthorizationHandler(Handler<?> aDefault, Connector connector, Authorizer auth) throws ConnectorException {
        super(aDefault, connector);
        this.auth = auth;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {

        if (auth.authorize(action, request, person)) {
            chain(action, request);
        } else {
            RealmResponse.send(request, 405, "Unauthorized");
        }
    }

}
