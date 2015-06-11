package net.realmproject.platform.api;


import java.io.IOException;

import net.objectof.Selector;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.rt.impl.IFn;
import net.realmproject.platform.api.utils.APIUtils;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceUI;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.model.Assignments;
import net.realmproject.platform.util.model.Courses;
import net.realmproject.platform.util.model.DeviceUIs;
import net.realmproject.platform.util.model.Devices;
import net.realmproject.platform.util.model.Persons;
import net.realmproject.platform.util.model.Sessions;
import net.realmproject.platform.util.model.Students;
import net.realmproject.platform.util.model.Teachers;


@Selector
public class IAdminAPIReceiver extends IFn {

    public IAdminAPIReceiver() {
        super();
    }

    /**
     * Is called by an admin user to get courses of a teacher.
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the teacher.
     * @throws IOException
     */

    @Selector
    public void getCoursesForTeacher(Person admin, HttpRequest request) throws IOException {

        // The teacher's label is included in the request and the teacher should
        // be retrieved using the label.
        Person teacher = (Person) APIUtils.getObjectFromRequest("Person", admin.tx(), request);

        if (teacher == null) {
            RealmResponse.send(request, 400, "Teacher cannot be null");
            return;
        }

        // Retrieve the courses of the teacher
        Iterable<Course> courses = Teachers.getCourses(admin.tx(), teacher);

        // Add the list of retrieved courses to the response
        APIUtils.addQueryResultToResponse(courses, request);

    }

    /**
     * Is called by an admin user to get the list of students enrolled in the
     * courses taught by a teacher.
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the teacher.
     * @throws IOException
     */

    @Selector
    public void getStudentsForTeacher(Person admin, HttpRequest request) throws IOException {

        // The teacher's label is included in the request and the teacher should
        // be retrieved using the label.
        Person teacher = (Person) APIUtils.getObjectFromRequest("Person", admin.tx(), request);

        if (teacher == null) {
            RealmResponse.send(request, 400, "Teacher cannot be null");
            return;
        }

        // Retrieve the list of students enrolled in the courses of the teacher
        Iterable<Person> students = Teachers.getStudents(admin.tx(), teacher);

        // Add the list of retrieved students to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by an admin user to get the list of students in the pending
     * lists of the courses taught by a teacher.
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the teacher.
     * @throws IOException
     */

    @Selector
    public void getPendingStudentsForTeacher(Person admin, HttpRequest request) throws IOException {

        // The teacher's label is included in the request and the teacher should
        // be retrieved using the label.
        Person teacher = (Person) APIUtils.getObjectFromRequest("Person", admin.tx(), request);

        if (teacher == null) {
            RealmResponse.send(request, 400, "Teacher cannot be null");
            return;
        }

        // Retrieve the list of pending students for the course of the teacher
        Iterable<Person> students = Teachers.getPendingStudents(admin.tx(), teacher);

        // Add the list of retrieved students to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by an admin user to get the list of students enrolled in a
     * course.
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the course.
     * @throws IOException
     */

    @Selector
    public void getStudentsForCourse(Person admin, HttpRequest request) throws IOException {

        // The course's label is included in the request and the course should
        // be retrieved using the label.
        Course course = (Course) APIUtils.getObjectFromRequest("Course", admin.tx(), request);

        if (course == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        // Retrieve the list of students enrolled in the course
        Iterable<Person> students = Courses.getStudents(admin.tx(), course);

        // Add the list of retrieved students to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by an admin user to get the list of pending students in a
     * course.
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the course.
     * @throws IOException
     */

    @Selector
    public void getPendingStudentsForCourse(Person admin, HttpRequest request) throws IOException {

        // The course's label is included in the request and the course should
        // be retrieved using the label.
        Course course = (Course) APIUtils.getObjectFromRequest("Course", admin.tx(), request);

        if (course == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        // Retrieve the list of pending students for the course
        Iterable<Person> students = Courses.getPendingStudents(admin.tx(), course);

        // Add the list of retrieved students to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by an admin user to get the list of assignments that are
     * designed for a device
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the device.
     * @throws IOException
     */

    @Selector
    public void getAsnsForDevice(Person admin, HttpRequest request) throws IOException {

        // The device's label is included in the request and the device should
        // be retrieved using the label.
        Device device = (Device) APIUtils.getObjectFromRequest("Device", admin.tx(), request);

        if (device == null) {
            RealmResponse.send(request, 400, "Device cannot be null");
            return;
        }

        // Retrieve the list of assignments that use the device
        Iterable<Assignment> asns = Devices.getAssignments(admin.tx(), device);

        // Add the list of retrieved assignments to the response
        APIUtils.addQueryResultToResponse(asns, request);

    }

    /**
     * Is called by an admin user to get the list of assignments that use a
     * device UI
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the device UI.
     * @throws IOException
     */

    @Selector
    public void getAsnsForDeviceUI(Person admin, HttpRequest request) throws IOException {

        // The device UI's label is included in the request and the device UI
        // should be retrieved using the label.
        DeviceUI deviceUI = (DeviceUI) APIUtils.getObjectFromRequest("DeviceUI", admin.tx(), request);

        if (deviceUI == null) {
            RealmResponse.send(request, 400, "DeviceUI cannot be null");
            return;
        }

        // Retrieve the list of assignments that use the device UI
        Iterable<Assignment> asns = DeviceUIs.getAssignments(admin.tx(), deviceUI);

        // Add the list of retrieved assignments to the response
        APIUtils.addQueryResultToResponse(asns, request);

    }

    /**
     * Is called by an admin user to get the list of sessions for an assignment
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the session.
     * @throws IOException
     */

    @Selector
    public void getSessionsForAsn(Person admin, HttpRequest request) throws IOException {

        // The assignment's label is included in the request and the assignment
        // should be retrieved using the label.
        Assignment asn = (Assignment) APIUtils.getObjectFromRequest("Assignment", admin.tx(), request);

        if (asn == null) {
            RealmResponse.send(request, 400, "Assignment cannot be null");
            return;
        }

        // Retrieve the list of sessions for the assignment
        Iterable<Session> sessions = Assignments.getSessions(admin.tx(), asn);

        // Add the list of retrieved sessions to the response
        APIUtils.addQueryResultToResponse(sessions, request);
    }

    /**
     * Is called by an admin user to get the list of assignments that are based
     * on an assignment template
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the assignment
     *            template
     * @throws IOException
     */

    @Selector
    public void getAsnsForAnsTemplate(Person admin, HttpRequest request) throws IOException {

        // The assignment template's label is included in the request and the
        // assignment template should be retrieved using the label.
        Assignment asnTemplate = (Assignment) APIUtils.getObjectFromRequest("Assignment", admin.tx(), request);

        if (asnTemplate == null) {
            RealmResponse.send(request, 400, "Assignment template cannot be null");
            return;
        }

        // Retrieve the list of assignments creates based on the assignment
        // template
        Iterable<Assignment> asns = Assignments.forTemplate(admin.tx(), asnTemplate);

        // Add the list of retrieved assignments to the response
        APIUtils.addQueryResultToResponse(asns, request);

    }

    /**
     * Is called by an admin user to get the list of courses that are based on a
     * course template
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the course template
     * @throws IOException
     */

    @Selector
    public void getCoursesForCourseTemplate(Person admin, HttpRequest request) throws IOException {

        // The course template's label is included in the request and the
        // course template should be retrieved using the label.
        Course crsTemplate = (Course) APIUtils.getObjectFromRequest("Course", admin.tx(), request);

        if (crsTemplate == null) {
            RealmResponse.send(request, 400, "Course template cannot be null");
            return;
        }

        // Retrieve the list of courses creates based on the course
        // template
        Iterable<Course> courses = Courses.forTemplate(admin.tx(), crsTemplate);

        // Add the list of retrieved courses to the response
        APIUtils.addQueryResultToResponse(courses, request);

    }

    /**
     * Is called by an admin user to get the list of devices that are owned by a
     * perosn
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the person
     * @throws IOException
     */

    @Selector
    public void getDevicesForOwner(Person admin, HttpRequest request) throws IOException {

        // The person's label is included in the request and the
        // person should be retrieved using the label.
        Person owner = (Person) APIUtils.getObjectFromRequest("Person", admin.tx(), request);

        if (owner == null) {
            RealmResponse.send(request, 400, "Person cannot be null");
            return;
        }

        // Retrieve the list of devices owned by the person
        Iterable<Device> devices = Persons.getOwnedDevices(admin.tx(), owner);

        // Add the list of retrieved devices to the response
        APIUtils.addQueryResultToResponse(devices, request);

    }

    /**
     * Is called by an admin user to get the list of devices that can use a
     * device UI
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the device UI
     * @throws IOException
     */

    @Selector
    public void getDevicesForDeviceUI(Person admin, HttpRequest request) throws IOException {

        // The device UI's label is included in the request and the
        // device UI should be retrieved using the label.
        DeviceUI deviceUI = (DeviceUI) APIUtils.getObjectFromRequest("DeviceUI", admin.tx(), request);

        if (deviceUI == null) {
            RealmResponse.send(request, 400, "DeviceUI cannot be null");
            return;
        }

        // Retrieve the list of devices that can use the device UI
        Iterable<Device> devices = DeviceUIs.getDevices(admin.tx(), deviceUI);

        // Add the list of retrieved devices to the response
        APIUtils.addQueryResultToResponse(devices, request);

    }

    /**
     * Is called by an admin user to get the list of stuents in a session
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the session
     * @throws IOException
     */

    @Selector
    public void getStudentsForSession(Person admin, HttpRequest request) throws IOException {

        // The session's label is included in the request and the
        // session should be retrieved using the label.
        Session session = (Session) APIUtils.getObjectFromRequest("Session", admin.tx(), request);

        if (session == null) {
            RealmResponse.send(request, 400, "Session cannot be null");
            return;
        }

        // Retrieve the list of students in the session
        Iterable<Person> students = Sessions.getStudents(admin.tx(), session);

        // Add the list of retrieved students to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by an admin user to get the list of assignments of a course
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request containing the info about the course
     * @throws IOException
     */

    @Selector
    public void getAsnsForCourse(Person admin, HttpRequest request) throws IOException {

        // The course's label is included in the request and the
        // course should be retrieved using the label.
        Course course = (Course) APIUtils.getObjectFromRequest("Course", admin.tx(), request);

        if (course == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        // Retrieve the list of assignments of the course
        Iterable<Assignment> asns = Courses.getAssignments(admin.tx(), course);

        // Add the list of retrieved assignments to the response
        APIUtils.addQueryResultToResponse(asns, request);

    }

    /**
     * Is called by an admin user to get the list of teachers
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request
     */

    @Selector
    public void getTeachers(Person admin, HttpRequest request) {

        // Retrieve the list of teachers and add them to the response
        APIUtils.addQueryResultToResponse(Teachers.enumerate(admin.tx()), request);
    }

    /**
     * Is called by an admin user to get the list of students
     * 
     * @param admin
     *            The Person object containing the admin info
     * @param request
     *            The http request
     */

    @Selector
    public void getStudents(Person admin, HttpRequest request) {

        // Retrieve the list of students and add them to the response
        APIUtils.addQueryResultToResponse(Students.enumerate(admin.tx()), request);
    }
}