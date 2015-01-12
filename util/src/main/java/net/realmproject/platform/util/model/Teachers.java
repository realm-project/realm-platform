package net.realmproject.platform.util.model;


import net.objectof.model.Transaction;
import net.objectof.model.query.IMultiQuery;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Relation;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Role;


public class Teachers {

    public static Iterable<Course> getCourses(Transaction tx, Person teacher) {
        return tx.query("Course", new IQuery("teachers", Relation.CONTAINS, teacher));
    }

    public static Iterable<Person> getStudents(Transaction tx, Person teacher) {
        return getStudents(tx, teacher, "enroledCourses");
    }

    public static Iterable<Person> getPendingStudents(Transaction tx, Person teacher) {
        return getStudents(tx, teacher, "pendingCourses");
    }

    private static Iterable<Person> getStudents(Transaction tx, Person teacher, String courseList) {

        Iterable<Course> courses = Teachers.getCourses(tx, teacher);
        IMultiQuery mq = new IMultiQuery(courseList, Relation.CONTAINS, courses);
        return tx.query("Person", mq);

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
