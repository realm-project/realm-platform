package net.realmproject.platform.security.authorization.authorizers;


import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Person;
import net.realmproject.model.schema.Session;
import net.realmproject.platform.security.authorization.Authorizer;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.model.Persons;
import net.realmproject.platform.util.model.Sessions;


/**
 * Reads the <b>next part</b> of the URL and compares it against the names of
 * the devices in the active sessions for the user. If no device with a matching
 * name is found in an active session, the user is denied access.
 * 
 * @author NAS
 *
 */
public class DeviceName implements Authorizer {

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {

        String deviceName = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

        for (Session session : Persons.getActiveSessions(person)) {
            if (!Sessions.isLive(session)) continue;

            for (Device device : session.getStation().getDevices()) {
                if (device.getName().equals(deviceName)) { return true; }
            }
        }

        return false;

    }

}
