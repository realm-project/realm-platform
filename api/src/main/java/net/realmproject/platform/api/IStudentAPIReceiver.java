package net.realmproject.platform.api;


import java.io.IOException;

import net.objectof.Selector;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.rt.impl.IFn;
import net.realmproject.platform.api.utils.APIUtils;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.model.Courses;
import net.realmproject.platform.util.model.Sessions;


@Selector
public class IStudentAPIReceiver extends IFn {

    public IStudentAPIReceiver() {
        super();
    }

    /**
     * Is called by a student user to get the list of assignments of a course.
     * The student should be enrolled in the course to be able to get the list
     * of assignments
     * 
     * @param student
     *            The Person object containing the student info
     * @param request
     *            The http request containing the info about the course
     * @throws IOException
     */

    @Selector
    public void getAsnsForCourse(Person student, HttpRequest request) throws IOException {

        // The course's label is included in the request and the
        // course should be retrieved using the label.
        Course c = (Course) APIUtils.getObjectFromRequest("Course", student.tx(), request);

        if (c == null) {
            request.getHttpResponse().sendError(400, "course is null!"); // "Bad request"
            return;
        }

        // Ensure that student is enrolled in the course
        if (student.getEnrolledCourses().contains(c)) {
            // Find assignments for this course
            Iterable<Assignment> assignments = Courses.getAssignments(student.tx(), c);

            // Add assignments to the response
            APIUtils.addQueryResultToResponse(assignments, request);

        } else { // Student is not enrolled in the course.
            request.getHttpResponse().sendError(403, "student has no access to information of this course!"); // "Forbidden"
        }
    }

    /**
     * Is called by a student user to get the list of students in a session. The
     * student should be a member of the session to be able to get the list of
     * students
     * 
     * @param student
     *            The Person object containing the student info
     * @param request
     *            The http request containing the info about the session
     * @throws IOException
     */

    @Selector
    public void getStudentsForSession(Person student, HttpRequest request) throws IOException {

        // The session's label is included in the request and the
        // session should be retrieved using the label.
        Session s = (Session) APIUtils.getObjectFromRequest("Session", student.tx(), request);

        if (s == null) {
            request.getHttpResponse().sendError(400, "session is null!"); // "Bad request"
            return;
        }

        // Ensure that student is a member of session
        if (!student.getSessions().contains(s)) {
            // Student is not a member of session. Set status to "Forbidden"
            request.getHttpResponse().sendError(403, "student has no access to information of this session!");
            return;
        }

        // Retrieve the members of the session
        Iterable<Person> students = Sessions.getStudents(student.tx(), s);

        // Add students to the response
        APIUtils.addQueryResultToResponse(students, request);
    }
}
