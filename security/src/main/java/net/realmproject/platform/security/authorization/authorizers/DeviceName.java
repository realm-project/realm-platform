package net.realmproject.platform.security.authorization.authorizers;


import java.util.Date;

import javax.servlet.http.HttpSession;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
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

    private final String DEVICE_NAME_AUTHORIZATION_KEY = DeviceName.class.getCanonicalName() + ":";

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {

        HttpSession httpSession = request.getHttpRequest().getSession(false);
        String deviceName = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

        // If this has been authorized any time in the last 15 seconds, don't
        // bother re-authorizing
        Long lastAuthorization = (Long) httpSession.getAttribute(authKey(deviceName));
        if (lastAuthorization != null && lastAuthorization > new Date().getTime() - 15) { return true; }

        for (Session session : Persons.getActiveSessions(person)) {
            if (!Sessions.isLive(session)) continue;

            for (Device device : session.getStation().getDevices()) {
                if (device.getName().equals(deviceName)) {
                    httpSession.setAttribute(authKey(deviceName), new Date().getTime());
                    return true;
                }
            }
        }

        httpSession.setAttribute(authKey(deviceName), null);
        return false;

    }

    private String authKey(String deviceName) {
        return DEVICE_NAME_AUTHORIZATION_KEY + deviceName;
    }

}
