package net.realmproject.platform.security.authentication;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmError;


/**
 * Validates a current session, to make sure that the user has logged in.
 * 
 * @author NAS
 *
 */
public class ISessionValidator extends ISessionHandler {

    public ISessionValidator(Handler<?> aDefault, Connector connector) throws ConnectorException {
        super(aDefault, connector);
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {

        if (person == null) {
            // 401 Unauthorized:
            // "specifically for use when authentication is required and has failed or has not yet been provided"
            request.getHttpResponse().getWriter().print(new RealmError("Unauthorized"));
            request.getHttpResponse().setStatus(401);
            return;
        }

        chain(action, request);

    }

}
