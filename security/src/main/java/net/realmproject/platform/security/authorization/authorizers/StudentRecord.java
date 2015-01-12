package net.realmproject.platform.security.authorization.authorizers;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.DeviceIO;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.security.authorization.RecordAuthorizer;
import net.realmproject.platform.util.model.DeviceCommands;
import net.realmproject.platform.util.model.DeviceIOs;

public class StudentRecord extends RecordAuthorizer {
	
	public StudentRecord(DatabaseRepository dbrepo) {
		super(dbrepo);
	}
	
	@Override
	protected boolean defaultResponse(Action action, HttpRequest request, Person person, RecordData data) {
		return false;
	}
	
	
	public boolean deviceui(Action action, HttpRequest request, Person person, RecordData data) {
		return readonly(data);
	}
	
	
	//People are only allowed to see their own Person record, not others...
	//TODO: How to let users get other user's names?
	public boolean person(Action action, HttpRequest request, Person person, RecordData data) {
		switch (data.mode) {
			
			case READ:
				Person thePerson = (Person) data.object;
				return person.getEmail().equals(thePerson.getEmail());
				
			default: return false;
		}
	}
	
	//Students can see sessions they belong to
	public boolean session(Action action, HttpRequest request, Person person, RecordData data) {
		if (data.mode != Mode.READ) return false;
		
		Session theSession = (Session) data.object;
		for (Session session : person.getSessions()) {
			if (session.id().equals(theSession.id())) return true;
		}
		
		return false;
	}

	//Students can see DeviceCommands of sessions they belong to
	public boolean devicecommand(Action action, HttpRequest request, Person person, RecordData data) {
		if (!readonly(data)) return false;
		
		DeviceCommand command = (DeviceCommand) data.object;
		Session session = DeviceCommands.getSession(person.tx(), command);
		if (session == null) return false;
		
		return person.getSessions().contains(session);
	}
	
	//Students can see DeviceIO of DeviceCommands of sessions they belong to
	public boolean deviceio(Action action, HttpRequest request, Person person, RecordData data) {
		if (!readonly(data)) return false;
		
		DeviceIO io = (DeviceIO) data.object;
		Session session = DeviceIOs.getSession(person.tx(), io);
		if (session == null) return false;
		
		return person.getSessions().contains(session);
		
	}
	
	//Student's are only allowed to see course offerings, not templates
	public boolean course(Action action, HttpRequest request, Person person, RecordData data) {
		if (data.mode != Mode.READ) return false;
		
		Course theCourse = (Course) data.object;
		return theCourse.getStartDate() != null;
	}
	
	//students can access assignments for sessions they are part of or for courses they're part of
	public boolean assignment(Action action, HttpRequest request, Person person, RecordData data) {
		if (data.mode != Mode.READ) return false;
		
		Assignment theAssignment = (Assignment) data.object;
		
		//is the assignment referenced by one of their sessions?
		for (Session session : person.getSessions()) {
			if (session.getAssignment().equals(theAssignment)) return true;
		}
		
		
		//is the assignment referenced by one of their courses?
		Transaction tx = repo().connect(action);
		for (Course course : person.getEnroledCourses()) {
    		for (Assignment assignment : tx.<Assignment>enumerate("Assignment")) {
    			if (assignment.getCourse().equals(course)) return true;
    		}
		}
		tx.close();
		
		return false;
	}
	
	public boolean role(Action action, HttpRequest request, Person person, RecordData data) {
		return readonly(data);
	}

}
