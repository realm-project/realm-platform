package tests;


import java.io.File;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.objectof.test.schema.person.Person;

import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.query.CNFQueryBuilder;
import net.objectof.model.query.IQuery;
import net.objectof.model.testing.ITestingPackage;

public class TestingRepoTest {

	public final IPackage schema = new ITestingPackage(IBaseMetamodel.INSTANCE,
		      new File("src/test/resources/models/test.xml"));
	
	{
		Transaction tx;
		Person p;
		
		tx = schema.connect("testing");
		p = tx.create("Person");
		p.setName("A");
		p = tx.create("Person");
		p.setName("B");
		p = tx.create("Person");
		p.setName("C");
		p = tx.create("Person");
		p.setName("D");
		tx.post();
		
		tx = schema.connect("testing");
		p = tx.create("Person");
		p.setName("E");
		p = tx.create("Person");
		p.setName("F");
		p = tx.create("Person");
		p.setName("G");
		p = tx.create("Person");
		p.setName("H");
		tx.post();
		
		tx = schema.connect("testing");
		p = tx.create("Person");
		p.setName("B");
		p = tx.create("Person");
		p.setName("B");
		p = tx.create("Person");
		p.setName("B");
		p = tx.create("Person");
		p.setName("B");
		tx.post();
		
	}
	
	@Test
	public void testQuery() {
		
		//create();
		Person p;
		
		Iterable<Person> ps;
		Iterator<Person> people;
		
		ps = schema.connect("test").query("Person", new IQuery("name", "A"));
		people = ps.iterator();
	    Assert.assertTrue(people.hasNext());
	    p = people.next();
	    Assert.assertEquals(p.getName(), "A");
	    Assert.assertFalse(people.hasNext());
	    
	    
	    ps = schema.connect("test").query("Person", new CNFQueryBuilder("name", "A").or("name", "D").build());
	    people = ps.iterator();
	    Assert.assertTrue(people.hasNext());
	    p = people.next();
	    Assert.assertEquals(p.getName(), "A");
	    Assert.assertTrue(people.hasNext());
	    p = people.next();
	    Assert.assertEquals(p.getName(), "D");
	    Assert.assertFalse(people.hasNext());
	    
	    
	    ps = schema.connect("test").query("Person", new IQuery("name", "B"));
	    int count = 0;
	    for (Person person : ps) {
	    	count++;
	    }
	    Assert.assertEquals(count, 5);
	    
	}
	
	@Test
	public void testEnum() {
		
		Iterable<Person> ps;
		
		ps = schema.connect("test").enumerate("Person");
		
		int count = 0;
		for (Person p : ps) {
			count++;
		}
		Assert.assertEquals(count, 12);
		
	}
	
	@Test
	public void testRetrieve() {
		
		Iterable<Person> ps;
		Iterator<Person> people;
		Person p1, p2;
		
		
		
		//First transaction
		ps = schema.connect("test").query("Person", new IQuery("name", "A"));
		people = ps.iterator();
	    Assert.assertTrue(people.hasNext());
	    p1 = people.next();
	    
	    p2 = schema.connect("test").retrieve("Person", "-1");
	    
	    Assert.assertEquals(p1, p2);
	    
	    
	    
	    //Second transaction
		ps = schema.connect("test").query("Person", new IQuery("name", "E"));
		people = ps.iterator();
	    Assert.assertTrue(people.hasNext());
	    p1 = people.next();
	    
	    p2 = schema.connect("test").retrieve("Person", "-5");
	    
	    Assert.assertEquals(p1, p2);
		
	}
	
}
