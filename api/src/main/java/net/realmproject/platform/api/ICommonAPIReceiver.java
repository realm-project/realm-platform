package net.realmproject.platform.api;


import java.io.IOException;

import net.objectof.Selector;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.rt.impl.IFn;
import net.realmproject.platform.api.utils.APIUtils;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.util.RealmError;
import net.realmproject.platform.util.model.Sessions;

import org.json.JSONException;


@Selector
public class ICommonAPIReceiver extends IFn {

    public ICommonAPIReceiver() {
        super();
    }

    /**
     * Is called by a user (admin, teacher, o student) to join a session
     * 
     * @param person
     *            The Person object containing the requesting person info
     * @param request
     *            The http request containing the info about the session.
     */

    @Selector
    public void joinSession(Person person, HttpRequest request) throws IOException, JSONException {

        // Extract the token from request
        String token;
        token = APIUtils.getStringFromRequest("token", request);

        // token must not be null
        if (token == null) {
            RealmError.send(request, 400, "Token cannot be null");
            return;
        }

        // Retrieve the session that owns this token
        Session s = Sessions.withToken(person.tx(), token);

        // session must not be null
        if (s == null) {
            RealmError.send(request, 400, "No session with that token");
            return;
        }

        // Ensure that the person is not already added to the session
        if (person.getSessions() != null && person.getSessions().contains(s)) {
            RealmError.send(request, 400, "Session is already added");
            return;
        }

        // Add session to person's sessions list
        IIndexed<Session> sessions = (IIndexed<Session>) person.getSessions();

        if (sessions == null) {
            sessions = person.tx().create("Person.sessions");
            person.setSessions(sessions);
        }

        sessions.add(s);
        person.tx().post();

    }

    /**
     * Is called by a user (admin, teacher, o student) to get the name of a
     * student
     * 
     * @param person
     *            The person object containing the info about requesting user
     * @param request
     *            http request containing the label of the student that her/his
     *            name is requested
     * @throws IOException
     */
    @Selector
    public void getStudentName(Person person, HttpRequest request) throws IOException {

        // The student's label is included in the request and the
        // person should be retrieved using the label.
        Person student = (Person) APIUtils.getObjectFromRequest("Person", person.tx(), request);

        // Ensure the retrieved person is nut null and her/his role is "student"
        if (student.getName() == null) {
            RealmError.send(request, 400, "Person cannot be null");
            return;
        }
        if (!student.getRole().getName().equals("student")) {
            RealmError.send(request, 400, "Person is not a student");
            return;
        }

        // Add the name of student to the response
        APIUtils.addStringResultToResponse(student.getName(), request);
    }
}
