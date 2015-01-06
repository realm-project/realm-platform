package net.realmproject.model.tests;

import net.objectof.aggr.Listing;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Course;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.DeviceCommand;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Session;

public class DeleteFromListTest {
	
	private static Package repo;
	
	private static void getPackage(String repoName) {
		ISqlDb db = new ISqlDb("realmDatabase");
		repo = db.getPackage(repoName);
		System.out.println(repo.getUniqueName());
	}
	
	public static void main(String[] args) {
		
		String repoName = "realmproject.net:1438/dev";
		
		try {
			getPackage(repoName);
			
			Transaction t = repo.connect("Azade");
			
//			Listing<Person> newTeachers = t.create("Course.teachers");
//			Course c = t.retrieve("Course", "1");
//			Listing<Person> ts = t.retrieve("Course.teachers", "2");
//			PrintObject.print(t, ts);
//			c.getTeachers().add(t.retrieve("Person", "2"));
//			Listing<Person> teachers = c.getTeachers();
//			teachers.add(t.retrieve("Person", "2"));
//			c.setTeachers(newTeachers);
			
//			Listing<Person> sharers = t.create("Device.sharers");
//			Device device = t.retrieve("Device", "1");
//			device.getSharers().add(t.retrieve("Person", "2"));
			
			Session s = t.retrieve("Session", "1");
			System.out.println(s.getCommands().size());
			PrintObject.print(t, s.getCommands());
			s.getCommands().remove(t.retrieve("DeviceCommand", 26));
//			s.getCommands().remove(4);
			PrintObject.print(t, s.getCommands());
//			Listing<DeviceCommand> sc = t.retrieve("Session.commands", "3");
//			Listing<DeviceCommand> dc = t.create("Session.commands");
//			s.getCommands().add(t.retrieve("DeviceCommand", "22"));
//			sc.add(t.retrieve("DeviceCommand", "24"));
			
//			Person p = t.retrieve("Person", "4");
//			p.getSessions().add(t.retrieve("Session", "3"));
			
			t.post();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
