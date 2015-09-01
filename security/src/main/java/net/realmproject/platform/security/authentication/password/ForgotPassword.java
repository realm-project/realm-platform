package net.realmproject.platform.security.authentication.password;


import java.io.IOException;

import javax.servlet.http.HttpSession;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmMedia;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.model.Persons;


public class ForgotPassword extends IRepoAwareHandler {

    public ForgotPassword(Connector connector) throws ConnectorException {
        super(connector);
    }

    protected void onExecute(Action action, HttpRequest request) throws IOException {

        // first check to make sure they don't already have a session
        HttpSession session = request.getHttpRequest().getSession(false);
        if (session != null) {
            RealmResponse.send(request, 401, "You are already logged in", true);
            return;
        }

        // grab the email from the document
        String json = RealmCorc.getJson(request.getHttpRequest().getReader());
        ForgotPasswordData forgot = DCMSerialize.deserialize(json, ForgotPasswordData.class);
        String email = forgot.username;

        // check to make sure a user with the given email exists.
        Transaction tx = repo().connect(getClass().getName());
        Person person = Persons.fromUsername(tx, email);
        if (person == null) {
            RealmResponse.send(request, 400, "Invalid email address", true);
            return;
        }

        // create a session, set a 20 minute timeout, and put the
        // password-reset-email attribute into it
        session = request.getHttpRequest().getSession(true);
        session.setMaxInactiveInterval(60 * 20);
        session.setAttribute("password-reset-username", email);

        // generate a password reset token, store it in the session and send it
        // to the given email address
        String token = RealmAuthentication.generateToken(6);
        // TODO: Remove this
        log().info("Password reset token: " + token);
        session.setAttribute("password-reset-token", token);
        RealmMedia.email(email, "Password Reset Token", "Your password reset token is: " + token);

    }
}
