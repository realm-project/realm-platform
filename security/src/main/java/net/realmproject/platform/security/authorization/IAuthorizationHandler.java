package net.realmproject.platform.security.authorization;


import javax.servlet.http.HttpSession;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmResponse;


/**
 * Attempts to authorize users (Person records) against a given
 * {@link Authorizer}
 * 
 * @author NAS 2014
 *
 */
public class IAuthorizationHandler extends ISessionHandler {

    private Authorizer auth;
    private static final long CACHE_TIME = 15 * 1000;

    public IAuthorizationHandler(Handler<?> aDefault, Connector connector, Authorizer auth) throws ConnectorException {
        super(aDefault, connector);
        this.auth = auth;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, String username) throws Exception {

        if (tryAuthorize(action, request, username)) {
            chain(action, request);
        } else {
            RealmResponse.send(request, 405, "Unauthorized");
        }

    }

    /*
     * Try to authorize a user based on username. First look to see if there is
     * a cached authorization value in their session. If not, try to authorize
     * by fetching the Person record from the repo and passing it to the
     * Authorizer component.
     */
    private boolean tryAuthorize(Action action, HttpRequest request, String username) {

        if (username == null) { return false; }
        HttpSession httpSession = request.getHttpRequest().getSession(false);

        // if this auth component isn't declining auth cacheing, check to see if
        // we've cached the auth for this user recently
        if (auth.cacheable()) {
            // If this has been authorized any time in the last 15 seconds for
            // this user, don't bother re-authorizing
            Long lastAuthorization = (Long) httpSession.getAttribute(authKey(username));
            if (lastAuthorization != null
                    && lastAuthorization > System.currentTimeMillis() - CACHE_TIME) { return true; }
        }

        // do a proper auth chech, since it doesn't look like auth has been
        // cached
        Transaction tx = repo().connect(ISessionHandler.class.getName());
        Person person = RealmCorc.getUser(tx, request);
        boolean isAuthed = auth.authorize(action, request, person);
        tx.close();

        // set the current time as the auth key for auth caching
        if (isAuthed) {
            httpSession.setAttribute(authKey(username), (Long) System.currentTimeMillis());
        }

        return isAuthed;

    }

    private String authKey(String username) {
        return IAuthorizationHandler.class.getCanonicalName() + ":" + auth.uuid() + ":" + username;
    }

}
