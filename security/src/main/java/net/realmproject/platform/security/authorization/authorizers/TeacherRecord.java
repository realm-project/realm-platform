package net.realmproject.platform.security.authorization.authorizers;


import java.io.IOException;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.security.authorization.RecordAuthorizer;


public class TeacherRecord extends RecordAuthorizer {

    public TeacherRecord(Connector connector) throws ConnectorException {
        super(connector);
    }

    @Override
    protected boolean defaultResponse(Action action, HttpRequest request, Person person, RecordData data) {
        return readonly(data);
    }

    // People are only allowed to see their own Person record, not others...
    // TODO: How to let users get other user's names?
    public boolean person(Action action, HttpRequest request, Person person, RecordData data) {
        return readonly(data);
    }

    public boolean session(Action action, HttpRequest request, Person person, RecordData data) throws IOException {
        if (data.mode == RecordAuthorizer.Mode.UPDATE) {
            Session theSession = (Session) data.object;

            // If person is a teacher of the corresponding course of the
            // session, let person to update the session
            if (theSession.getAssignment().getCourse().getTeachers().contains(person)) return true;
        }

        if (readonly(data)) { return true; }

        return false;
    }
}
