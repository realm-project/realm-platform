package net.realmproject.platform.security.authentication.password;


import java.io.IOException;

import javax.servlet.http.HttpSession;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.RealmSerialize;
import net.realmproject.platform.util.model.Persons;


public class ResetPassword extends IRepoAwareHandler {

    public ResetPassword(Connector connector) throws ConnectorException {
        super(connector);
    }

    protected void onExecute(Action action, HttpRequest request) throws IOException {

        // Look up an existing session.
        HttpSession session = request.getHttpRequest().getSession(false);
        if (session == null) {
            RealmResponse.send(request, 401, "No password request session found");
        }

        // grab the data from the reset password document
        String json = RealmCorc.getJson(request.getHttpRequest().getReader());
        ResetPasswordData reset = RealmSerialize.deserialize(json, ResetPasswordData.class);

        // fetch the stored username and token from the password reset request.
        // Make sure the fields both exist, and make sure the 'person' field
        // used by a regular login session does not
        String storedUsername = (String) session.getAttribute("password-reset-username");
        String storedToken = (String) session.getAttribute("password-reset-token");
        if (storedUsername == null || storedToken == null) {
            RealmResponse.send(request, 401, "Invalid password reset session");
            return;
        }
        Object storedPerson = session.getAttribute("person");
        if (storedPerson != null) {
            RealmResponse.send(request, 401, "Password cannot be reset by a logged in user");
            return;
        }

        // make sure the new password is good enough
        if (reset.password == null || reset.password.length() < 6) {
            RealmResponse.send(request, 403, "Password must be at least 6 characters in length");
            return;
        }

        // make sure the emails and tokens match
        if (storedUsername.equals(reset.username) && storedToken.equals(reset.token)) {
            // Retrieve the user and change the password
            String salt = RealmAuthentication.generateSalt();
            String passwordHashed = RealmAuthentication.hash(reset.password, salt);

            Transaction tx = repo().connect(getClass().getName());
            Person person = Persons.fromUsername(tx, storedUsername);

            person.setSalt(salt);
            person.setPwdHashed(passwordHashed);

            tx.post();

            RealmResponse.sendOk(request, "Password updated successfully");

        } else {
            RealmResponse.send(request, 400, "Invalid username or token");
            return;
        }

    }
}
