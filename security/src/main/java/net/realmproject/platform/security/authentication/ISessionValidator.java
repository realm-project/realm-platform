package net.realmproject.platform.security.authentication;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmResponse;


/**
 * Validates a current session, to make sure that the user has logged in.
 * 
 * @author NAS
 *
 */
public class ISessionValidator extends ISessionHandler {

    private Log log = LogFactory.getLog(getClass());

    public ISessionValidator(Handler<?> aDefault, Connector connector) throws ConnectorException {
        super(aDefault, connector);
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, String username) throws Exception {

        if (username == null) {
            log.debug("username was null: 401 Unauthorized");
            // 401 Unauthorized:
            // "specifically for use when authentication is required and has
            // failed or has not yet been provided"
            request.getHttpResponse().getWriter().print(new RealmResponse("Unauthorized"));
            request.getHttpResponse().setStatus(401);
            return;
        }

        chain(action, request);

    }

}
