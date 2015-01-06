package net.realmproject.model.tests;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import net.objectof.aggr.Listing;
import net.objectof.aggr.Mapping;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.model.query.IQuery;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Assignment;
import net.realmproject.model.schema.Course;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.DeviceIO;
import net.realmproject.model.schema.DeviceUI;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Role;
import net.realmproject.model.schema.Session;


public class CreateFreshRealmRepo {

    private static Package repo;

    private static void createPackage(String repoName) {
        IPackage schema = new ISourcePackage(IBaseMetamodel.INSTANCE, new File("src/main/resources/packages/realm.xml"));
        ISqlDb db = new ISqlDb("realmDatabase");
        repo = db.createPackage(repoName, IRip.class.getName(), schema);
        System.out.println(repo.getUniqueName());
    }

    private static void getPackage(String repoName) {
        ISqlDb db = new ISqlDb("realmDatabase");
        repo = db.getPackage(repoName);
        System.out.println(repo.getUniqueName());
    }

    private static void createDeviceCommandStates(Transaction t) {
        @SuppressWarnings("unchecked")
        Listing<DeviceIO> states = (Listing<DeviceIO>) t.create("DeviceCommand.states");
    }

    private static void addStateToDeviceCommand() {
        Transaction stateTx = repo.connect("Azade");
        DeviceIO state = stateTx.create("DeviceIO");
        state.setJson("Some JSON string!");
        state.setUnixtime(new Date().getTime());
        stateTx.post();

        Transaction statesTx = repo.connect("Azade");
        Listing<DeviceIO> states = statesTx.retrieve("DeviceCommand.states", "1");
        // states.set(states.size(), state);
        states.add(state);
        statesTx.post();

        // Transaction commandTx = repo.connect("Azade");
        // DeviceCommand command = commandTx.retrieve("DeviceCommand", "64");
        // command.setStates(states);
        // commandTx.post();
    }

    private static void createPerson(Transaction t, String name, String email, String pwdHashed, String salt,
            String role) {

        Person person = (Person) t.create("Person");

        person.setName(name);
        person.setEmail(email);
        person.setPwdHashed(pwdHashed);
        person.setSalt(salt);

        if (role.compareTo("student") == 0) person.setRole((Role) t.retrieve("Role", "1"));
        else if (role.compareTo("teacher") == 0) person.setRole((Role) t.retrieve("Role", "2"));
        else System.out.println("createPerson: Unknown role!");
    }

    private static void nullPerson(Transaction t, String label) {
        Person person = t.retrieve("Person", label);
        person.setEmail(null);
        person.setEnroledCourses(null);
        person.setName(null);
        person.setPendingCourses(null);
        person.setPwdHashed(null);
        person.setRole(null);
        person.setSalt(null);
        person.setSessions(null);
    }

    private static void createRoles(Transaction t) {
        ((Role) t.create("Role")).setName("teacher");
        ((Role) t.create("Role")).setName("student");
        ((Role) t.create("Role")).setName("admin");
    }

    private static void createCourse(Transaction t) {
        // Course course = (Course) t.create("Course");
        // Course course = t.retrieve("Course", "1");
        // course.setName("Robotic Manipulators");
        // @SuppressWarnings("unchecked")
        // IIndexed<Person> teachers = (IIndexed<Person>)
        // t.create("Course.teachers");
        // teachers.add((Person)(t.retrieve("Person", "14")));
        // course.setTeachers(teachers);
        // course.setDescription("Introduction to the basic principles and techniques involved in modeling, simulating and controlling rigid-link manipulators. Includes forward and inverse kinematics, manipulator dynamics, control of robot manipulators.");
        // course.setStartDate(new IMoment("2014-09-01"));
        // course.setEndDate(new IMoment("2014-12-16"));
        // course.setId("MSE 4401");
    }

    private static void createAssignment1(Transaction t) {
        Assignment a = (Assignment) t.create("Assignment");
        // Assignment a = (Assignment) t.retrieve("Assignment", "1");
        a.setCourse((Course) t.retrieve("Course", "1"));
        a.setId("Lab Exercise 1");
        a.setName("Creating a Trajectory");
        // a.setDescription(null);
        // a.setStartDate(new IMoment("2014-09-01"));
        // a.setEndDate(new IMoment("2014-12-30"));
    }

    private static void createAssignment2(Transaction t) {
        Assignment a = (Assignment) t.create("Assignment");
        // Assignment a = (Assignment) t.retrieve("Assignment", "2");
        a.setCourse((Course) t.retrieve("Course", "1"));
        a.setId("Lab Exercise 2");
        a.setName("Forward Kinematics");
        // a.setDescription("This is the description for the forward kinematics Assignment.");
        // a.setStartDate(new IMoment("2014-09-01"));
        // a.setEndDate(new IMoment("2014-12-30"));
    }

    private static void createAssignment3(Transaction t) {
        // Assignment a = (Assignment) t.create("Assignment");
        Assignment a = (Assignment) t.retrieve("Assignment", "3");
        // a.setCourse((Course) t.retrieve("Course", "1"));
        // a.setId("Lab Exercise 3");
        // a.setName("Inverse Kinematics");
        a.setDescription(null);
        a.setStartDate(null);
        a.setEndDate(null);
    }

    private static void createAssignment4(Transaction t) {
        Assignment a = (Assignment) t.create("Assignment");
        // Assignment a = (Assignment) t.retrieve("Assignment", "1");
        a.setCourse((Course) t.retrieve("Course", "1"));
        a.setId("Lab Exercise 4");
        a.setName("Dynamic Respose");
        // a.setDescription("This is the description for the dynamic response and control Assignment.");
        // a.setStartDate(new IMoment("2014-09-01"));
        // a.setEndDate(new IMoment("2014-12-30"));
    }

    private static void createSession1(Transaction t) {
        Session s = (Session) t.retrieve("Session", "6");
        // Session s = (Session) t.create("Session");
        // s.setAssignment((Assignment) t.retrieve("Assignment", "1"));
        s.setDuration(129600L); // Three months
        // s.setStartTime(new IMoment("2014-08-06T15:00:00Z"));
        Mapping<String, Device> devices = s.getDevices();
        devices.clear();
        devices.put("mico", (Device) t.retrieve("Device", "3"));
        s.setDevices(devices);
        // s.setSessionToken("jGTg-rev9-JhRM");
    }

    private static void createSession2(Transaction t) {
        Session s = (Session) t.retrieve("Session", "2");
        // Session s = (Session) t.create("Session");
        // s.setAssignment((Assignment)t.retrieve("Assignment", "2"));
        s.setDuration(129600L); // Three months
        // s.setStartTime(new IMoment("2014-08-06T15:00:00Z"));
        // @SuppressWarnings("unchecked")
        // IMapped<Device> devices = (IMapped<Device>)
        // t.create("Session.devices");
        // devices.put("mico", (Device) t.retrieve("Device", "1"));
        // s.setDevices(devices);
        // s.setSessionToken("vUtK-TM3l-2m4L");
    }

    private static void createSession3(Transaction t) {
        // Session s = (Session) t.create("Session");
        Session s = (Session) t.retrieve("Session", "3");
        // s.setAssignment((Assignment)t.retrieve("Assignment", "3"));
        s.setDuration(129600L); // Three months
        // s.setStartTime(new IMoment("2014-08-06T15:00:00Z"));
        // @SuppressWarnings("unchecked")
        // IMapped<Device> devices = (IMapped<Device>)
        // t.create("Session.devices");
        // devices.put("mico", (Device) t.retrieve("Device", "1"));
        // s.setDevices(devices);
        // s.setSessionToken("JJcz-pnMZ-27jc");
    }

    private static void createDeviceUIs(Transaction t) {
        String[] names = { "Creating a Trajectory", "Forward Kinematics", "Inverse Kinematics", "Dynamic Respose" };
        String[] urls = { "teachPoints", "forwardKinematics", "inverseKinematics", "dynamicResponse" };

        for (int i = 0; i < names.length; i++) {
            DeviceUI deviceUI = t.create("DeviceUI");
            deviceUI.setName(names[i]);
            deviceUI.setUrl(urls[i]);
        }
    }

    private static void setDeviceUIofAssignments(Transaction t) {
        String[] names = { "Creating a Trajectory", "Forward Kinematics", "Inverse Kinematics", "Dynamic Respose" };

        for (int i = 0; i < names.length; i++) {
            DeviceUI deviceUI = (DeviceUI) t.query("DeviceUI", new IQuery("name", names[i])).iterator().next();
            Assignment asn = (Assignment) t.query("Assignment", new IQuery("name", names[i])).iterator().next();

            asn.setDeviceUI(deviceUI);
        }
    }

    private static void setAssignmentsOfSessions(Transaction t) {
        String[] tokens = { "vUtK-TM3l-2m4L", "jGTg-rev9-JhRM", "JJcz-pnMZ-27jc", null };
        String[] assignments = { "teach points", "forward kinematics", "inverse kinematics",
                "dynamic response and control" };

        for (int i = 0; i < tokens.length - 1; i++) {
            Session session = (Session) t.query("Session", new IQuery("sessionToken", tokens[i])).iterator().next();
            Assignment asn = (Assignment) t.query("Assignment", new IQuery("name", assignments[i])).iterator().next();

            session.setAssignment(asn);
        }
    }

    private static void createMicoDevice(Transaction t) {
        // Device device = (Device) t.create("Device");
        Device device = (Device) t.retrieve("Device", "3");
        device.setName("testDevice2");
        device.setOwner((Person) t.retrieve("Person", "1"));

        // for (int i = 1; i <= 4; i++) {
        // Assignment a = t.retrieve("Assignment", Integer.toString(i));
        // Listing<Device> devices = t.create("Assignment.devices");
        // devices.add(device);
        // a.setDevices(devices);
        // }
    }

    private static void nullObject(Object object) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("set")) try {
                Object[] args = new Object[1];
                args[0] = null;
                m.invoke(object, args);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createRepo() {
        Transaction t = repo.connect("Azade");

        // for (int j = 11; j < 30; j ++) {
        // DeviceCommand p = t.retrieve("DeviceCommand", Integer.toString(j));
        // nullObject(p);
        // }

        // createRoles(t);
        // createPerson(t, "qqq", "qqq@uwo.com",
        // "1594244d52f2d8c12b142bb61f47bc2eaf503d6d9ca8480cae9fcf112f66e4967dc5e8fa98285e36db8af1b8ffa8b84cb15e0fbcf836c3deb803c13f37659a60",
        // "world", "teacher");
        // createPerson(t, "Person B", "b@b.com", "", "world", "student");
        // createCourse(t);
        // createAssignment1(t);
        // createAssignment2(t);
        // createAssignment3(t);
        // createAssignment4(t);
        // createMicoDevice(t);
        createSession1(t);
        // createSession2(t);
        // createSession3(t);
        // createDeviceCommandStates(t);
        // createDeviceUIs(t);
        // setDeviceUIofAssignments(t);
        // setAssignmentsOfSessions(t);

        t.post();
        t.close();
    }

    public static void testRepo() {

        Transaction t = repo.connect("Azade");

        // System.out.println("repo: " + repo.getUniqueName());
        // PrintObject.print(t, t.retrieve("Role", "1"));
        // PrintObject.print(t, t.retrieve("Role", "2"));
        // PrintObject.print(t, t.retrieve("Person", "1"));
        // PrintObject.print(t, t.retrieve("Person", "2"));
        // PrintObject.print(t, t.retrieve("Person", "3"));
        // PrintObject.print(t, t.retrieve("Person", "4"));
        // PrintObject.print(t, t.retrieve("Course", "1"));
        // PrintObject.print(t, t.retrieve("Assignment", "1"));
        // PrintObject.print(t, t.retrieve("Assignment", "2"));
        // PrintObject.print(t, t.retrieve("Assignment", "3"));
        // PrintObject.print(t, t.retrieve("Assignment", "4"));
        // PrintObject.print(t, t.retrieve("Device", "1"));
        // PrintObject.print(t, t.retrieve("Session", "1"));
        // PrintObject.print(t, t.retrieve("Session", "2"));
        // PrintObject.print(t, t.retrieve("Session", "3"));

        // for (int i = 1; i < 20; i ++)
        // CreateRepo.print(t, t.retrieve("DeviceCommand.states",
        // Integer.toString(i)));

        // for (int i = 1; i <= 30; i ++)
        // PrintObject.print(t, t.retrieve("DeviceCommand",
        // Integer.toString(i)));

        // for (int i = 51; i <= 52; i ++)
        // PrintObject.print(t, t.retrieve("DeviceIO", Integer.toString(i)));
        //
        // for (int i = 1; i <= 10; i ++)
        // PrintObject.print(t, t.retrieve("Session", Integer.toString(i)));
        //
        // for (int i = 1; i <= 10; i ++)
        // PrintObject.print(t, t.retrieve("Assignment", Integer.toString(i)));

    }

    public static void main(String[] args) {

        // Branches: dev, ui, master, realm, students, objectof
        String[] repos = { "dev", "master", "ui", "realm", "students", "objectof" };

        for (int i = 0; i < 1; i++) {

            String repoName = "realmproject.net:1438/" + repos[i];

            try {

                getPackage(repoName);

                createRepo();

                // testRepo();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
