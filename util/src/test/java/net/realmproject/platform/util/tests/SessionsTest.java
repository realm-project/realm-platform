package net.realmproject.platform.util.tests;


import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import net.objectof.connector.TempSQLiteRepo;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.realmproject.platform.model.RealmSchema;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.model.Sessions;

import org.junit.Test;


public class SessionsTest {

    public Package schema;

    {
        try {
            schema = TempSQLiteRepo.forSchema(RealmSchema.get());
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Transaction tx;
        Person p;
        Session s;
        Date aDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(2013, 11, 9);
        Date bDate = cal.getTime();

        cal.set(3000, 11, 9);
        Date cDate = cal.getTime();

        Long a = (long) 100;

        tx = schema.connect("testing");
        p = tx.create("Person");
        p.setName("personA");
        s = tx.create("Session");
        s.setStartTime(aDate);
        s.setDuration(a);
        tx.post();

        tx = schema.connect("testing");
        s = tx.create("Session");
        s.setStartTime(bDate);
        s.setDuration(a);
        tx.post();

        tx = schema.connect("testing");
        s = tx.create("Session");
        s.setStartTime(cDate);
        s.setDuration(a);
        tx.post();

    }

    @Test
    public void testIsLive() {
        Transaction tx = schema.connect(null);
        Session s;

        Boolean expected = true;
        s = tx.retrieve("Session", "1");
        Boolean actual = Sessions.isLive(s);

        Assert.assertEquals(expected, actual);

        expected = false;
        s = tx.retrieve("Session", "2");
        actual = Sessions.isLive(s);

        Assert.assertEquals(expected, actual);

        expected = false;
        s = tx.retrieve("Session", "3");
        actual = Sessions.isLive(s);

        Assert.assertEquals(expected, actual);
    }

}
