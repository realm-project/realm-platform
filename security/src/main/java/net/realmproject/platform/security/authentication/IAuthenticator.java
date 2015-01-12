package net.realmproject.platform.security.authentication;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpSession;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.schema.Person;


/**
 * Handles authentication of a user when logging in. Expects a JSON Payload
 * described by the LoginInfo class
 * 
 * @author nathaniel
 *
 */

public class IAuthenticator extends AbstractAuthenticator<LoginInfo> {

    public IAuthenticator(DatabaseRepository dbrepo) {
        super(dbrepo, LoginInfo.class);
    }

    @Override
    protected void onAuthenticate(Action action, HttpRequest request, Person person, LoginInfo info, boolean success)
            throws IOException {

        if (success) {

            log.debug("Successfully logged in " + info.username);
            Writer writer = new StringWriter();
            person.send("application/json", writer);
            log.debug(writer.toString());

            // get a session, or create a new one, and store the Person record
            // in it.
            HttpSession session = request.getHttpRequest().getSession(true);
            session.setAttribute("person", person.getEmail());

            // write back the person record
            person.send("application/json", request.getWriter());

        } else {
            // 401 Unauthorized:
            // "specifically for use when authentication is required and has failed or has not yet been provided"
            request.getHttpResponse().sendError(401);
        }

    }

}
