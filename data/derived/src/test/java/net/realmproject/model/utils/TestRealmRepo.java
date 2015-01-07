package net.realmproject.model.utils;


import java.io.File;
import java.util.Random;

import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Assignment;
import net.realmproject.model.schema.Course;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Role;
import net.realmproject.model.schema.Session;


public class TestRealmRepo {

    public static void createRepo() {
        String repositoryName = "test.objectof.org:1407/test/";

        IPackage schema = new ISourcePackage(IBaseMetamodel.INSTANCE, new File("src/main/resources/packages/realm.xml"));
        ISqlDb db = new ISqlDb("stressTestDatabase");
        Package repo = db.createPackage(repositoryName, IRip.class.getName(), schema);
        System.out.println(repo.getUniqueName());
    }

    public static Package repo() {
        // ISqlDb db = new ISqlDb("stressTestDatabase");
        // return db.getPackage("test.objectof.org:1407/test/");

        ISqlDb db = new ISqlDb("realmDatabase");
        return db.getPackage("realmproject.net:1438/dev");
    }

    public static void retrieve(String aKind, int number) {
        Transaction t = repo().connect("Azade");

        for (int i = 1; i <= number; i++) {
            Object aObject = t.retrieve(aKind, Integer.toString(i));
            PrintObject.print(t, aObject);
        }
        t.close();
    }

    static void createCourseTeachers(Transaction aTransaction, Course aCourse, int index) {
        // Create teachers
        @SuppressWarnings("unchecked")
        IIndexed<Person> teachers = (IIndexed<Person>) aTransaction.create("Course.teachers");
        // Attach to aCourse
        aCourse.setTeachers(teachers);
        // Create the teachers and assign it:
        for (int j = index; j < index + 2; j++) {
            teachers.add((Person) aTransaction.retrieve("Person", Integer.toString(j % 7 + 6)));
        }
    }

    static void createStudentCourses(Transaction aTransaction, Person student, int index) {
        IIndexed<Course> enroledCourses = aTransaction.create("Person.enroledCourses");
        IIndexed<Course> pendingCourses = aTransaction.create("Person.pendingCourses");

        student.setEnroledCourses(enroledCourses);
        student.setPendingCourses(pendingCourses);

        Random randomGenerator = new Random();
        int enroledCoursesNo = randomGenerator.nextInt(5);
        int pendingCoursesNo = randomGenerator.nextInt(5);

        for (int j = index; j < index + enroledCoursesNo; j++) {
            enroledCourses.add(aTransaction.retrieve("Course", Integer.toString(j % 30 + 5)));
        }

        for (int j = index + 5; j < index + 5 + pendingCoursesNo; j++) {
            pendingCourses.add(aTransaction.retrieve("Course", Integer.toString(j % 30 + 5)));
        }
    }

    private static void createRoles(Transaction t) {
        t.<Role> create("Role").setName("teacher");
        t.<Role> create("Role").setName("student");
    }

    private static void createCourseTemplates(Transaction t, int no) {
        for (int i = 1; i <= no; i++) {
            Course course = t.create("Course");
            course.setId("CT" + i);
            course.setName("Course Template" + i);
            course.setDescription("This is the description of " + course.getName());
        }
    }

    private static void createCourses(Transaction t, int no) {

        for (int i = 1; i <= no; i++) {
            Course course = t.create("Course");
            course.setCourse(t.retrieve("Course", Integer.toString(i % 3 + 2)));
            course.setId("C" + i);
            course.setName("Course " + i);
            course.setDescription("This is the description of " + course.getName());
            for (int j = 0; j < 3; j++) {
                createCourseTeachers(t, course, i);
            }
            course.setStartDate(new IMoment("2014-09-01"));
            course.setEndDate(new IMoment("2014-12-30"));
        }
    }

    private static void createTeachers(Transaction t, int no) {
        for (int i = 1; i <= no; i++) {
            Person teacher = t.create("Person");
            teacher.setName("Teacher " + i);
            teacher.setEmail("teacher" + i + "@email.com");
            teacher.setPwdHashed("teacher" + i + "PwdHashed");
            teacher.setSalt("teacher" + i + "salt");
            teacher.setRole(t.retrieve("Role", "1"));
        }
    }

    private static void createStudents(Transaction t, int no) {
        for (int i = 1; i <= no; i++) {
            Person student = t.create("Person");
            student.setName("Student " + i);
            student.setEmail("student" + i + "@email.com");
            student.setPwdHashed("student" + i + "PwdHashed");
            student.setSalt("student" + i + "salt");
            student.setRole((Role) t.retrieve("Role", "2"));
            createStudentCourses(t, student, i);
        }
    }

    private static void createAssignments(Transaction t, int no) {
        for (int i = 1; i <= no; i++) {
            Assignment a = t.create("Assignment");
            a.setCourse(t.retrieve("Course", "5"));
            a.setName("Assignment " + i);
            a.setDescription("This is the description of Assignment " + i);
        }
    }

    private static void createPerson(Transaction t, String name, String email, String pwdHashed, String salt,
            String role) {

        Person person = t.create("Person");

        person.setName(name);
        person.setEmail(email);
        person.setPwdHashed(pwdHashed);
        person.setSalt(salt);

        if (role.compareTo("student") == 0) person.setRole(t.retrieve("Role", "1"));
        if (role.compareTo("teacher") == 0) person.setRole(t.retrieve("Role", "2"));
        else System.out.println("createPerson: Unknown role!");
    }

    // private static void createPoints(Transaction<Object> t, int no) {
    // for (int i = 1; i <= no; i ++) {
    // Position position = (Position)t.create("Position");
    // position.setEndEffectorWidth(i * 10.0);
    // position.setTimestamp(new IMoment());
    // }
    // }

    // private static void createCommands(Transaction<Object> t, int no) {
    // Transaction<Object> t = repo.connect("Azade");
    // for (int i = 1; i <= no; i ++) {
    // Command command = (Command)t.create("Command");
    //
    // @SuppressWarnings("unchecked")
    // IMapped<Position> positions =
    // (IMapped<Position>)t.create("Command.outputPositions");
    //
    // for (int j = 0; j < 5; j ++) {
    // Position position = (Position)t.retrieve("Position", Integer.toString((i
    // + j) % 10));
    // positions.put("Position", position);
    // }
    //
    // command.setOutputPositions(positions);
    // }
    // }

    private static void createADevice(Transaction t) {
        Device device = (Device) t.create("Device");

        device.setName("Mico");

        Person person = (Person) t.retrieve("Person", "1");

        device.setOwner(person);

        PrintObject.print(t, device);
    }

    private static void createSession(Transaction t) {
        Session s = (Session) t.create("Session");
        s.setAssignment((Assignment) t.retrieve("Assignment", "1"));
        s.setDuration(10080L); // One week
        s.setStartTime(new IMoment("2014-09-16T17:00:00Z"));
    }

    public static void main(String[] args) {

        // createRepo();

        Package repo = repo();
        Transaction t = repo.connect("Azade");

        // for (int i = 0; i < 5; i ++)
        // createSession(t);

        ((Person) t.retrieve("Person", "4")).getSessions().add(t.retrieve("Session", "5"));
        ((Person) t.retrieve("Person", "5")).getSessions().add(t.retrieve("Session", "5"));
        ((Person) t.retrieve("Person", "6")).getSessions().add(t.retrieve("Session", "5"));
        ((Person) t.retrieve("Person", "15")).getSessions().add(t.retrieve("Session", "5"));
        ((Person) t.retrieve("Person", "12")).getSessions().add(t.retrieve("Session", "5"));

        // Session s4 = t.retrieve("Session", "4");
        // s4.setAssignment(null);
        // s4.setCommands(null);
        // s4.setDevices(null);
        // s4.setDuration(null);
        // s4.setSessionToken(null);
        // s4.setStartTime(null);

        // System.out.println(s4.id().label());

        t.post();
        t.close();
    }

}
