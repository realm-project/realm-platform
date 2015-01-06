package net.realmproject.model.tests;

import java.io.File;

import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.impl.aggr.IMapped;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Course;
import net.realmproject.model.schema.Device;

public class TransactionTest {
	
	private static Package repo;
			
	private static void getPackage(String repoName) {
		ISqlDb db = new ISqlDb("realmDatabase");
		repo = db.getPackage(repoName);
		System.out.println(repo.getUniqueName());
	}
	
	public static void modifyRepo1() {
		Transaction t = repo.connect("Azade");
		
//		IMapped<Device> devices = t.create("Session.devices");
		IMapped<Device> devices = t.retrieve("Session.devices", "1");
		PrintObject.print(t, devices);
		
		Device device = t.create("Device");
		device.setName("dev4");
		
		devices.put("device4", device);
		PrintObject.print(t, devices);
		
		t.post();
		t.close();
	}
	
	public static void modifyRepo2(int i) {
		Transaction t = repo.connect("Azade");

//		Course course = t.create("Course");
//		course.setName("test course");			
		
//		IIndexed<Course> enroledCourses = t.create("Person.enroledCourses");
		IIndexed<Course> enroledCourses = t.retrieve("Person.enroledCourses", "4");
//		PrintObject.print(t, enroledCourses);
		
		Course course2 = t.create("Course");
		course2.setName("test course2");
		
		enroledCourses.add(course2);
		PrintObject.print(t, enroledCourses);
		
		t.post();
		t.close();
	}
	
	public static void testRepo() {
		
		Transaction t = repo.connect("Azade");
		System.out.println("testRepo - repo: " + repo.getUniqueName());
		
//		IMapped<Device> devices = t.retrieve("Session.devices", "1");
//		PrintObject.print(t, devices);
	
//		for (int i = 1; i <= 4; i ++) {
//			IIndexed<Course> enroledCourses = t.retrieve("Person.enroledCourses", Integer.toString(i));
//			PrintObject.print(t, enroledCourses);
//		}
		
//		t.post();
		t.close();
	}
	
	public static void main(String[] args) {
		
		String repoName = "realmproject.net:1441/transactiontest2";
		
		try {
//			getPackage(repoName);
//
//			for (int i = 1 ; i <= 1; i ++)
//				modifyRepo2(i);
			
			testRepo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
