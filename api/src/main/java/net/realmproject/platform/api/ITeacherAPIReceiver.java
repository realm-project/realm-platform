package net.realmproject.platform.api;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import net.objectof.Selector;
import net.objectof.aggr.Composite;
import net.objectof.aggr.Listing;
import net.objectof.aggr.impl.IListing;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IPackage;
import net.objectof.rt.impl.IFn;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.platform.api.datatypes.CreateSession;
import net.realmproject.platform.api.datatypes.EditSession;
import net.realmproject.platform.api.utils.APIUtils;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Course;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.DeviceCommand;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.schema.Station;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmResponse;
import net.realmproject.platform.util.model.Assignments;
import net.realmproject.platform.util.model.Courses;
import net.realmproject.platform.util.model.Devices;
import net.realmproject.platform.util.model.Persons;
import net.realmproject.platform.util.model.Sessions;
import net.realmproject.platform.util.model.Teachers;


@Selector
public class ITeacherAPIReceiver extends IFn {

    public ITeacherAPIReceiver() {
        super();
    }

    /**
     * Is called by a teacher user to get the list of courses that are taught by
     * the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request
     */

    @Selector
    public void getCoursesForTeacher(Person teacher, HttpRequest request) {

        // Retrieve the list of courses that the teacher teaches for
        Iterable<Course> courses = Teachers.getCourses(teacher.tx(), teacher);

        // Add courses to the response
        APIUtils.addQueryResultToResponse(courses, request);
    }

    /**
     * Is called by a teacher user to get the list of all students enrolled in
     * all courses that are taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request
     */

    @Selector
    public void getStudentsForTeacher(Person teacher, HttpRequest request) {

        // Retrieve the list of all students enrolled in all courses that are
        // taught by the teacher
        Iterable<Person> students = Teachers.getStudents(teacher.tx(), teacher);

        // Add students to response
        APIUtils.addQueryResultToResponse(students, request);
    }

    /**
     * Is called by a teacher user to get the list of students enrolled in a
     * course that is taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request
     * @throws IOException
     */

    @Selector
    public void getStudentsForCourse(Person teacher, HttpRequest request) throws IOException {

        // The course's label is included in the request and the
        // course should be retrieved using the label.
        Course c = (Course) APIUtils.getObjectFromRequest("Course", teacher.tx(), request);

        if (c == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        // Ensure that the teacher is a teacher of the course
        if (!c.getTeachers().contains(teacher)) {
            RealmResponse.send(request, 403, "Teacher is not authorized to access this course");
            return;
        }

        // Retrieve the list of students of the course
        Iterable<Person> students = Courses.getStudents(teacher.tx(), c);

        // Add students list to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by a teacher user to get the list of pending students in all
     * courses that are taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request
     */

    @Selector
    public void getPendingStudentsForTeacher(Person teacher, HttpRequest request) {

        // Retrieve the list of pending students in all courses that are taught
        // by the teacher
        Iterable<Person> students = Teachers.getPendingStudents(teacher.tx(), teacher);

        // Add students list to the response
        APIUtils.addQueryResultToResponse(students, request);
    }

    /**
     * Is called by a teacher user to get the list of pending students in a
     * course that is taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the course info
     * @throws IOException
     */

    @Selector
    public void getPendingStudentsForCourse(Person teacher, HttpRequest request) throws IOException {

        // The course's label is included in the request and the
        // course should be retrieved using the label.
        Course c = (Course) APIUtils.getObjectFromRequest("Course", teacher.tx(), request);

        if (c == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        // Ensure that the teacher is a teacher of the course
        if (!c.getTeachers().contains(teacher)) {
            RealmResponse.send(request, 403, "Teacher is not authorized to access this course");
            return;
        }

        // Retrieve the list of pending students of the course
        Iterable<Person> students = Courses.getPendingStudents(teacher.tx(), c);

        // Add students list to the response
        APIUtils.addQueryResultToResponse(students, request);

    }

    /**
     * Is called by a teacher user to get the list of assignments of a course
     * that is taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the course info
     * @throws IOException
     */

    @Selector
    public void getAsnsForCourse(Person teacher, HttpRequest request) throws IOException {

        // The course's label is included in the request and the
        // course should be retrieved using the label.
        Course c = (Course) APIUtils.getObjectFromRequest("Course", teacher.tx(), request);

        if (c == null) {
            RealmResponse.send(request, 400, "Course cannot be null");
            return;
        }

        Listing<Person> teachers = c.getTeachers();

        // Ensure that the teacher is a teacher of the course
        // if (c.getTeachers().contains(teacher)) {
        boolean isTeacher = false;

        for (Person t : teachers) {
            if (t.equals(teacher)) {
                isTeacher = true;
                break;
            }
        }

        if (!isTeacher) {
            RealmResponse.send(request, 403, "User is not a teacher");
            return;
        }

        // Retrieve the list of assignments of the course
        Iterable<Assignment> asns = Courses.getAssignments(teacher.tx(), c);

        // Add assignments to the response
        APIUtils.addQueryResultToResponse(asns, request);

    }

    /**
     * Is called by a teacher user to get the list of members of a session. The
     * session should belongs to a course that is taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the session info
     * @throws IOException
     */

    @Selector
    public void getStudentsForSession(Person teacher, HttpRequest request) throws IOException {

        // The session's label is included in the request and the
        // session should be retrieved using the label.
        Session s = (Session) APIUtils.getObjectFromRequest("Session", teacher.tx(), request);

        if (s == null) {
            RealmResponse.send(request, 400, "Assignment cannot be null");
            return;
        }

        // Ensure that teacher is the teacher of the course that session belongs
        // to
        if (!s.getAssignment().getCourse().getTeachers().contains(teacher)) {
            RealmResponse.send(request, 403, "Teacher is not authorized to access this course");
            return;
        }

        // Retrieve members of the session
        Iterable<Person> students = Sessions.getStudents(teacher.tx(), s);

        // Add members to the response
        APIUtils.addQueryResultToResponse(students, request);
    }

    /**
     * Is called by a teacher user to get the list of sessions of an assignment.
     * The assignment should belongs to a course that is taught by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the session info
     * @throws IOException 
     */

    @Selector
    public void getSessionsForAsn(Person teacher, HttpRequest request) throws IOException {

        // The assignments's label is included in the request and the
        // assignments should be retrieved using the label.
        Assignment a = (Assignment) APIUtils.getObjectFromRequest("Assignment", teacher.tx(), request);

        if (a == null) {
            RealmResponse.send(request, 400, "Assignment cannot be null");
            return;
        }

        // Ensure that teacher is the teacher of the course that the assignment
        // belongs to
        if (!a.getCourse().getTeachers().contains(teacher)) {
            RealmResponse.send(request, 403, "Teacher is not authorized to access this assignment.");
            return;
        }

        // Retrieve sessions of assignment
        Iterable<Session> sessions = Assignments.getSessions(teacher.tx(), a);

        // Add sessions to the response
        APIUtils.addQueryResultToResponse(sessions, request);

    }
    
    @Selector
    public void getSessionObjectsForAssignment(Person teacher, HttpRequest request) throws IOException {
    	// The assignments's label is included in the request and the
        // assignments should be retrieved using the label.
        Assignment a = (Assignment) APIUtils.getObjectFromRequest("Assignment", teacher.tx(), request);

        if (a == null) {
            RealmResponse.send(request, 400, "Assignment cannot be null");
            return;
        }

        // Ensure that teacher is the teacher of the course that the assignment
        // belongs to
        if (!a.getCourse().getTeachers().contains(teacher)) {
            RealmResponse.send(request, 403, "Teacher is not authorized to access this assignment.");
            return;
        }

        // Retrieve sessions of assignment
        Iterable<Session> sessions = Assignments.getSessions(teacher.tx(), a);
        
        APIUtils.addObjectQueryResultToResponse(teacher.tx(), sessions, request);
    }
    

    /**
     * Is called by a teacher user to get the list of sessions for a device. The
     * teacher should be the owner of the device. If the deivce is null, the
     * method finds the sessions for all devices owned by the teacher
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the device info
     * @throws IOException 
     */

    @Selector
    public void getSessionsForDeviceOwner(Person teacher, HttpRequest request) throws IOException {

        Transaction tx = teacher.tx().getPackage().connect(teacher);

        // The device's label is included in the request and the
        // device should be retrieved using the label.
        Device device = (Device) APIUtils.getObjectFromRequest("Device", teacher.tx(), request);

        // Retrieve devices owned by this owner
        Iterable<Device> devices = Persons.getOwnedDevices(teacher.tx(), teacher);

        if (device != null) { // If device is not null, check if the device
        	// is owned by the teacher or not
        	boolean contains = false;
        	for (Device d : devices)
        		if (d.equals(device)) {
        			contains = true;
        			break;
        		}

        	if (!contains) {
        		RealmResponse.send(request, 403, "User is not authorized to access this device");
        		return;
        	}

        	// Retrieve the sessions for this device and add them to
        	// response
        	Iterable<Session> sessions = Devices.getSessions(tx, device);

        	APIUtils.addQueryResultToResponse(sessions, request);

        } else { // Retrieve sessions for all devices owned by this owner
        	// and add them to response
        	ArrayList<Session> sessions = new ArrayList<Session>();

        	for (Device d : devices) {
        		Iterable<Session> deviceSessions = Devices.getSessions(tx, d);
        		for (Session s : deviceSessions) {
        			if (!sessions.contains(s)) sessions.add(s);
        		}
        	}

        	// Add sessions to response
        	APIUtils.addQueryResultToResponse(sessions, request);
        }
    }

    /**
     * Is called by a teacher user to get the list devices she/he owns.
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request
     */

    @Selector
    public void getDevicesForOwner(Person teacher, HttpRequest request) {

        // Retrieve list of devices owned by the teacher
        Iterable<Device> devices = Persons.getOwnedDevices(teacher.tx(), teacher);

        // Add devices to the response
        APIUtils.addQueryResultToResponse(devices, request);
    }

    /**
     * Is called by a teacher user to remove a session
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the session info
     * @throws IOException 
     */

    @Selector
    public void removeSession(Person teacher, HttpRequest request) throws IOException {

        Transaction tx = teacher.tx().getPackage().connect(teacher);

        // The session's label is included in the request and the
        // session should be retrieved using the label.
        Session session = (Session) APIUtils.getObjectFromRequest("Session", tx, request);

        if (session == null) {
			RealmResponse.send(request, 400, "Session connot be null!");
            return;
        }
        
        if (!APIUtils.hasWriteAccessToSession(teacher, session)) {
			RealmResponse.send(request, 403, "Person has no write access to this session!");
            return;
        }

        // Remove session from all persons' session lists
        Iterable<Person> students = Sessions.getStudents(tx, session);

        for (Person student : students) {
            student.getSessions().remove(session);
        }

        // Clear all the fields of session
        APIUtils.nullSession(tx, session.id().label().toString());

        tx.post();
        tx.close();
    }
    
    /**
     * Is called by a teacher user to edit a session
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the session info
     * @throws IOException 
     */
    
    @Selector
    public void editSession(Person teacher, HttpRequest request) throws IOException {
    	
    	Transaction tx = teacher.tx().getPackage().connect(teacher);
    	
        try {
        	// Create a EditSession object using the JSON body of the request
			EditSession editSessionRequest = RealmCorc.getJsonAsData(request.getReader(), EditSession.class);
			
			// Retrieve session. Session can not be null
	        Session session = (Session) APIUtils.getObjectFromRequest("Session", tx, request);
	        
	        if (session == null) {
	        	RealmResponse.send(request, 400, "Session connot be null!");
	        	return;
	        }

	        // Check if teacher has write access to session or not
	        if (!APIUtils.hasWriteAccessToSession(teacher, session)) {
	        	RealmResponse.send(request, 403, "Person has no write access to this session!");
	        	return;
	        }
	        
	        // Retrieve assignment
	        Assignment asn = null;
	        
	        if (editSessionRequest.assignment == null) {
//	        	session.setAssignment(null);
	        } else {
	        	asn = (Assignment) APIUtils.getObjectFromRequest("Assignment", tx, request);

	        	if (asn != null)
	        		session.setAssignment(asn);
	        	else {
	        		RealmResponse.send(request, 400, "Assignment is null!");
	        	}
	        }

	        // Retrieve station
	        Station station = null;

	        if (editSessionRequest.station == null) {
//	        	session.setStation(null);
	        } else {
	        	station = (Station) APIUtils.getObjectFromRequest("Station", tx, request);

	        	if (station != null)
	        		session.setStation(station);
	        	else {
	        		RealmResponse.send(request, 400, "Station is null!");
	        	}
	        }
	        
	        if (editSessionRequest.duration != 0)
	        	session.setDuration(editSessionRequest.duration);
        	
	        if (editSessionRequest.startTime != null) {
	        	 session.setStartTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(editSessionRequest.startTime ));
	        }
	        
	        tx.post();
	        tx.close(); 
			
		} catch (ServletException | ParseException e) {
			e.printStackTrace();
			RealmResponse.sendError(request, e.getMessage());
		}
    }

    /**
     * Is called by a teacher user to create sessions
     * 
     * @param teacher
     *            The Person object containing the teacher info
     * @param request
     *            The http request containing the information about time and
     *            date of sessions to be created
     * @throws IOException 
     */

    @Selector
    public void createSession(Person teacher, HttpRequest request) throws IOException {
    	
    	int numberOfCreatedSessions = 0;

        Transaction tx = teacher.tx().getPackage().connect(teacher);

        try {

            String createSessionType = null;

            // Create a CreateSession object using the JSON body of the request
            CreateSession createSessionRequest = RealmCorc.getJsonAsData(request.getReader(), CreateSession.class);

            // The request should contain either time.single, or time.bulk
            // The request should contain either date.range, or date.list
            if ((createSessionRequest.time.single != null && createSessionRequest.time.bulk != null)
                    || (createSessionRequest.date.range != null && createSessionRequest.date.list != null)) {
                RealmResponse.send(request, 400, "Request should contain either time.single or time.bulk");
                return;
            }

            // Retrieve assignment
            Assignment asn = null;
            String asnLabel = APIUtils.getLabel(createSessionRequest.assignment);

            if (asnLabel == null) {
                RealmResponse.send(request, 400, "Assignment label cannot be null");
                return;
            }
            asn = tx.retrieve("Assignment", asnLabel);
            if (asn == null) {
                RealmResponse.send(request, 400, "Assignment cannot be null");
                return;
            }

            // Retrieve station
            String stationLabel = APIUtils.getLabel(createSessionRequest.station);
            if (stationLabel == null) {
                RealmResponse.send(request, 400, "Assignment label cannot be null");
                return;
            }
            Station station = tx.retrieve("Station", stationLabel);
            if (station == null) {
                RealmResponse.send(request, 400, "Station cannot be null");
                return;
            }

            if (createSessionRequest.date.range != null) {
                List<Date> dates = APIUtils.createDateList(createSessionRequest.date.range.start,
                        createSessionRequest.date.range.end);
                
                if (createSessionRequest.time.single != null) 
                	createSessionType = "single";
                else // sessionRequest.time.bulk != null
                	createSessionType = "bulk";

                for (Date date : dates) {
//                	int daysLen = sessionRequest.date.range.days.length;
                	
                	// If "days" list is empty, or the day of "date" is included in the "days" list, create session for the "date"
                    if (createSessionRequest.date.range.days.length == 0 || APIUtils.dateIsInDays(date, createSessionRequest.date.range.days)) {
                    	numberOfCreatedSessions += APIUtils.createSession(tx, asn, date, station, createSessionRequest, createSessionType);
                    }
                }
            } else {// sessionRequest.date.list != null
                for (String strDate : createSessionRequest.date.list) {

                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);

                    if (createSessionRequest.time.single != null) createSessionType = "single";
                    else // sessionRequest.time.bulk != null
                    createSessionType = "bulk";

                    numberOfCreatedSessions += APIUtils.createSession(tx, asn, date, station, createSessionRequest, createSessionType);
                }
            }

            tx.post();
            tx.close();
            
            APIUtils.addStringResultToResponse(Integer.toString(numberOfCreatedSessions), request);            
        }
        catch (ParseException | ServletException e) {
            e.printStackTrace();
            RealmResponse.sendError(request, e.getMessage());
        }
    }
    
    @Selector
    public void getSessionsForCourse(Person teacher, HttpRequest request) throws IOException {
    	ArrayList<Session> sessions = new ArrayList<Session>();
    	
    	Course course = (Course) APIUtils.getObjectFromRequest("Course", teacher.tx(), request);
    	if (course == null) {
    		RealmResponse.send(request, 400, "Course cannot be null!");
    		return;
    	}

    	// Ensure that the teacher is a teacher of the course
    	if (!course.getTeachers().contains(teacher)) {
    		RealmResponse.send(request, 403, "Teacher is not authorized to access this course!");
    		return;
    	}

    	// Retrieve the list of assignments of the course
    	Iterable<Assignment> assignments = Courses.getAssignments(teacher.tx(), course);

    	// Retrieve sessions of assignments
    	for (Assignment assignment : assignments) {

    		Iterable<Session> asnSessions = Assignments.getSessions(teacher.tx(), assignment);

    		for (Session session : asnSessions)
    			sessions.add(session);

    	}

    	// Add sessions to response
    	APIUtils.addQueryResultToResponse(sessions, request);
    }
    
    @Selector
    public void getStationsForTeacher(Person teacher, HttpRequest request) {
    	Iterable<Station> stations = Teachers.getStations(teacher.tx(), teacher);
    	APIUtils.addQueryResultToResponse(stations, request);
    }
}