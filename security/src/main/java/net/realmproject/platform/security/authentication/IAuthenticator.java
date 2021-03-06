package net.realmproject.platform.security.authentication;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpSession;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.util.RealmResponse;


/**
 * Handles authentication of a user when logging in. Expects a JSON Payload
 * described by the LoginInfo class
 * 
 * @author NAS
 *
 */

public class IAuthenticator extends AbstractAuthenticator<LoginInfo> {

    public IAuthenticator(Connector connector) throws ConnectorException {
        super(connector, LoginInfo.class);
    }

    @Override
    protected void onAuthenticate(Action action, HttpRequest request, Person person, LoginInfo info, boolean success)
            throws IOException {

        try {

            if (success) {

                log.debug("Successfully logged in " + info.username);
                Writer writer = new StringWriter();
                person.send("application/json", writer);
                log.debug(writer.toString());

                // get a session, or create a new one, and store the Person
                // record
                // in it.
                HttpSession session = request.getHttpRequest().getSession(true);
                session.setAttribute("person", person.getEmail());

                // write back the person record
                person.send("application/json", request.getWriter());

            } else {
                log.debug("401 Unauthorized");
                // 401 Unauthorized:
                // "specifically for use when authentication is required and has
                // failed or has not yet been provided"
                request.getHttpResponse().getWriter().print(new RealmResponse("Login Failed", true));
                request.getHttpResponse().setStatus(401);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
