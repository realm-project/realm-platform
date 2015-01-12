package net.realmproject.platform.security.authentication;


import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Role;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.RealmSerialize;


public class IAccountCreator extends IRepoAwareHandler {

    public IAccountCreator(DatabaseRepository dbrepo) {
        super(dbrepo);
        System.out.println("IAccountCreator");
    }

    @Override
    protected void onExecute(Action action, HttpRequest request) throws Exception {

        System.out.println("IAccountCreator - onExecute");

        String json = RealmCorc.getJson(request.getHttpRequest().getReader());

        System.out.println("IAccountCreator - json:" + json);

        SignUpInfo info = RealmSerialize.deserialize(json, SignUpInfo.class);
        Transaction tx = repo().connect(action);

        Iterable<Person> existingUsers = tx.query("Person", new IQuery("email", info.username));
        if (existingUsers.iterator().hasNext()) {
            request.getHttpResponse().sendError(403, "That user already exists");
            return;
        }

        System.out.println("IAccountCreator - the username is accepted!");

        Role studentRole = RealmRepo.queryHead(tx, "Role", new IQuery("name", "student"));
        if (studentRole == null) throw new NullPointerException();

        String salt = RealmAuthentication.generateSalt();
        String passwordHashed = RealmAuthentication.hash(info.password, salt);

        Person person = tx.create("Person");
        person.setEmail(info.username);
        person.setName(info.fullname);
        person.setRole(studentRole);
        person.setPwdHashed(passwordHashed);
        person.setSalt(salt);

        tx.post();
        tx.close();

    }

}
