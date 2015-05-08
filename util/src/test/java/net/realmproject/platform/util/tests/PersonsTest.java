package net.realmproject.platform.util.tests;


import java.util.stream.Collectors;

import junit.framework.Assert;
import net.objectof.aggr.impl.IListing;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.query.IQuery;
import net.realmproject.platform.model.RealmSchema;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmRepo;
import net.realmproject.platform.util.model.Persons;
import net.realmproject.platform.util.model.Sessions;
import net.realmproject.platform.util.testing.ITestingPackage;

import org.junit.Test;


public class PersonsTest {

    public final IPackage schema = new ITestingPackage(IBaseMetamodel.INSTANCE, RealmSchema.get());

    {
        Transaction tx;
        Person p;

        tx = schema.connect("testing");
        p = tx.create("Person");
        p.setName("personA");
        p.setSessions(new IListing<Session>(Session.class));

        Device dev = tx.create("Device");
        dev.setOwner(p);

        tx.post();

    }

    @Test
    public void testFromUsername() {
        Transaction tx = schema.connect(null);
        String username;
        Boolean expected, actual;
        Person p;
        actual = null;

        // First
        username = null;
        expected = false;
        if (Persons.fromUsername(tx, username) == null) {
            actual = false;
        }
        Assert.assertEquals(expected, actual);

        // Second
        username = "USERNAME";
        expected = true;
        p = RealmRepo.queryHead(tx, "Person", new IQuery("email", username));
        if (Persons.fromUsername(tx, username) == p) {
            actual = true;
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetActiveSessions() {
        Transaction tx = schema.connect(null);
        Person p, person;
        Iterable<Session> s;
        Boolean expected, actual;

        expected = true;
        p = tx.retrieve("Person", "-1");
        person = tx.retrieve("Person", "-1");
        s = person.getSessions().stream().filter(Sessions::isLive).collect(Collectors.toList());

        if (Persons.getActiveSessions(p).equals(s)) {
            actual = true;
        } else {
            actual = false;
        }

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetOwnedDevices() {
        Transaction tx = schema.connect(null);
        Person owner;
        Iterable<Device> devQuery;

        owner = tx.retrieve("Person", "-1");
        devQuery = tx.query("Device", new IQuery("owner", owner));

        Device d1, d2;
        d1 = devQuery.iterator().next();
        d2 = Persons.getOwnedDevices(tx, owner).iterator().next();
        Assert.assertSame(d1, d2);

    }

}
