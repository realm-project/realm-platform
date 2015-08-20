package net.realmproject.platform.util.model;


import java.util.ArrayList;
import java.util.Collection;

import net.objectof.model.Transaction;
import net.objectof.model.query.IMultiQuery;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Role;
import net.realmproject.platform.schema.Station;


public class Teachers {

    public static Iterable<Course> getCourses(Transaction tx, Person teacher) {
        return tx.query("Course", new IQuery("teachers", Relation.CONTAINS, teacher));
    }

    public static Iterable<Person> getStudents(Transaction tx, Person teacher) {
        return getStudents(tx, teacher, "enrolledCourses");
    }

    public static Iterable<Person> getPendingStudents(Transaction tx, Person teacher) {
        return getStudents(tx, teacher, "pendingCourses");
    }

    private static Iterable<Person> getStudents(Transaction tx, Person teacher, String courseList) {

        Iterable<Course> courses = Teachers.getCourses(tx, teacher);
        IMultiQuery mq = new IMultiQuery(courseList, Relation.CONTAINS, courses);
        return tx.query("Person", mq);

    }
    
    public static Iterable<Station> getStations(Transaction tx, Person teacher) {
    	
    	ArrayList<Station> stations = new ArrayList<Station>();
    	Boolean deviceIsNotAccessible = false;
    	    	
    	Iterable<Station> allStations = tx.enumerate("Station");
    	
    	for (Station station : allStations) {
    		deviceIsNotAccessible = false;
    		
    		if (station.getOwner().equals(teacher)) {
    			stations.add(station);
    			continue;
    		}
    		
    		Collection<Device> devices = station.getDevices().values();
    		for (Device device : devices) {
    			if (!(device.getOwner().equals(teacher) || device.getSharers().contains(teacher))) {
    				deviceIsNotAccessible = true;
    				break;
    			}
    		}
    		if (deviceIsNotAccessible) continue;
    		else stations.add(station);
    	}
    	
    	return stations;
    }

    public static Iterable<Person> enumerate(Transaction tx) {
        return tx.query("Person", new IQuery("role", Teachers.role(tx)));
    }

    public static Role role(Transaction tx) {
        Role role = Roles.get(tx, "admin");
        if (role == null) throw new NullPointerException();
        return role;
    }

}
