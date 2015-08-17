package net.realmproject.platform.security.authentication;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Role;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.RealmResponse;


public class IAccountCreator extends IRepoAwareHandler {

    public IAccountCreator(Connector connector) throws ConnectorException {
        super(connector);
    }

    @Override
    protected void onExecute(Action action, HttpRequest request) throws Exception {

        String json = RealmCorc.getJson(request.getHttpRequest().getReader());

        SignUpInfo info = DCMSerialize.deserialize(json, SignUpInfo.class);
        Transaction tx = repo().connect(action);

        Iterable<Person> existingUsers = tx.query("Person", new IQuery("email", info.username));
        if (existingUsers.iterator().hasNext()) {
            RealmResponse.send(request, 403, "User already exists");
            return;
        }

        log().trace("IAccountCreator - the username is accepted!");

        // If it is the first account, set the role to "admin"
        Iterable<Person> iter = tx.enumerate("Person");
        Role role = null;
        if (!iter.iterator().hasNext()) {
            log().info("Creating the first account. The role is set to admin.");
            role = RealmRepo.queryHead(tx, "Role", new IQuery("name", "admin"));
        } else {
            role = RealmRepo.queryHead(tx, "Role", new IQuery("name", "student"));
        }

        if (role == null) throw new NullPointerException();

        if (info.password == null || info.password.length() < 6) {
            RealmResponse.send(request, 403, "Password must be at least 6 characters in length");
            return;
        }

        String salt = RealmAuthentication.generateSalt();
        String passwordHashed = RealmAuthentication.hash(info.password, salt);

        Person person = tx.create("Person");
        person.setEmail(info.username);
        person.setName(info.fullname);
        person.setRole(role);
        person.setPwdHashed(passwordHashed);
        person.setSalt(salt);

        tx.post();
        tx.close();

    }
}
