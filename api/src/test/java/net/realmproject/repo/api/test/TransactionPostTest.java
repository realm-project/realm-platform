package net.realmproject.repo.api.test;


import net.objectof.aggr.Listing;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Session;
import net.realmproject.service.data.model.RealmSchema;
import net.realmproject.util.model.Sessions;


public class TransactionPostTest {

    public static void joinSession(Person person, String token) {

        // token must not be null
        if (token == null) { return; }

        // session must not be null
        Session session = Sessions.withToken(person.tx(), token);
        if (session == null) { return; }

        // don't re-add sessions
        if (person.getSessions() != null && person.getSessions().contains(session)) { return; }

        Listing<Session> sessions = person.getSessions();
        if (sessions == null) {
            sessions = person.tx().create("Person.sessions");
            person.setSessions(sessions);
        }

        sessions.add(session);
        person.tx().post();

        // IIndexed<Session> sessions = (IIndexed<Session>)
        // person.getSessions();
        // if (sessions == null) {
        // Transaction tx3 = person.tx().getPackage().connect(person);
        // sessions = tx3.create("Person.sessions");
        // Person personTx3 = tx3.retrieve(person.id());
        // personTx3.setSessions(sessions);
        // tx3.post();
        // }
        //
        // Transaction tx2 = person.tx().getPackage().connect(person);
        // sessions = tx2.retrieve(sessions.id());
        // s = tx2.retrieve(s.id());
        //
        // sessions.add(s);
        // tx2.post();

    }

    public static void createPersonSession(Person person) {

        // don't re-create Person.sessions
        if (person.getSessions() != null) { return; }

        Listing<Session> sessions = person.tx().create("Person.sessions");
        person.setSessions(sessions);

        person.tx().post();
    }

    private static void createPersons() throws Exception {
        Package repo = RealmSchema.getTestPackage();
        Transaction t = repo.connect("Azade");

        for (int i = 11; i < 21; i++) {
            Person p = t.create("Person");
            p.setName("Student " + i);
        }

        t.post();
    }

    private static void printSessions(Listing<Session> sessions) {
        for (Session s : sessions)
            System.out.println(s.getUniqueName());
    }

    public static void main(String[] args) throws Exception {

        Package repo = RealmSchema.getTestPackage();

        // createPersons();

        // Transaction t = repo.connect("Azade");
        // Person person = t.retrieve("Person", "7");
        // person.setEmail("student7@uwo.ca");
        // person.setName("Student 7");
        // person.setRole(t.retrieve("Role", "1"));
        // person.setSalt("077d4bce75b38b79c72e97a48115e5970e9d287e243e8edd8448c0c6f257a558e2e19215c63cbc87978edf9913988a4f8e9e7dbcbb18c5b48adfa39d4bd0a698");
        // person.setPwdHashed("3e1ab2fdc4d35e26d645ee9b99c740cf5122c29d74ecfaf7259d1d2b3540f73645f74e6f14c7a527b81ffea5c96c8065552d9cd41fe96524fa7b9a0766d311b1");
        // person.setSessions(null);
        // t.post();

        // Person person = t.create("Person");

        // Test set the Person.sessions to a person with no Person.sessions
        // System.out.println(person.getUniqueName());
        // if (person.getSessions() == null) {
        // System.out.println("Before setting Person.sessions, sessions is null!");}
        // else {
        // System.out.println("Before setting Person.sessions, sessions is not null!");}
        // createPersonSession(person);
        // if (person.getSessions() == null) {
        // System.out.println("After setting Person.sessions, sessions is null!");}
        // else {
        // System.out.println("After setting Person.sessions, sessions is not null!");}
        //
        // Transaction t2 = repo.connect("Azade");
        // Person person2 = t2.retrieve("Person",
        // Long.toString((long)(person.id().label())));
        // System.out.println(person2.getUniqueName());
        // if (person2.getSessions() == null)
        // System.out.println("With second tx: sessions is null!");
        // else {
        // System.out.println("With second tx: sessions is not null!");
        // printSessions(person.getSessions());
        // }

        // // Test add a session to a person
        // System.out.println(person.getUniqueName());
        //
        // Listing<Session> sessions = person.getSessions();
        //
        // if (sessions == null)
        // System.out.println("Before add session: sessions is null!");
        // else System.out.println("Before add session: sessions is not null!");
        //
        // joinSession(person, "JJcz-pnMZ-27jc"); // Join Session-3
        //
        // if (person.getSessions() == null)
        // System.out.println("After add session: sessions is null!");
        // else {
        // System.out.println("After add session: sessions is not null!");
        // printSessions(person.getSessions());
        // }
        //
        // Transaction t2 = repo.connect("Azade");
        // Person person2 = t2.retrieve(person.id());
        // System.out.println(person2.getUniqueName());
        // if (person2.getSessions() == null)
        // System.out.println("With second tx: sessions is null!");
        // else {
        // System.out.println("With second tx: sessions is not null!");
        // printSessions(person2.getSessions());
        // }

        Transaction t = repo.connect("Azade");
        Session session = t.retrieve("Session", "1");
        session.setCommands(null);
        // Listing<DeviceCommand> commands = session.getCommands();
        // System.out.println(commands.size());
        // for (int index = commands.size() - 1 ; index > 2 ; index --)
        // commands.remove(index);
        // System.out.println(commands.size());
        // for (DeviceCommand dc : session.getCommands())
        // System.out.println(dc.getUniqueName());
        t.post();
    }
}
