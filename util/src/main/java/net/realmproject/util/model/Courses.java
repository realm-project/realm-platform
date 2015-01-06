package net.realmproject.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.model.schema.Assignment;
import net.realmproject.model.schema.Course;
import net.realmproject.model.schema.Person;

public class Courses {

	public static Iterable<Assignment> getAssignments(Transaction tx, Course course) {
		return tx.query("Assignment", new IQuery("course", course));
	}
	
	public static Iterable<Person> getStudents(Transaction tx, Course course) {
		return tx.query("Person", new IQuery("enroledCourses", Relation.CONTAINS, course));
	}

	public static Iterable<Person> getPendingStudents(Transaction tx, Course course) {
		return tx.query("Person", new IQuery("pendingCourses", Relation.CONTAINS, course));
	}
	
	public static Iterable<Course> forTemplate(Transaction tx, Course template) {
		return tx.query("Course", new IQuery("course", template));
	}
	
	
}
