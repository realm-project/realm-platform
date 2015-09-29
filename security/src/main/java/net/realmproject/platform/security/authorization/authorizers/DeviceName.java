package net.realmproject.platform.security.authorization.authorizers;


import java.util.UUID;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.schema.Station;
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
public class DeviceName extends AbstractAuthorizer {

    @Override
    public String cacheString(Action action, HttpRequest request) {
        return RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());
    }

    @Override
    public boolean cacheable() {
        return true;
    }

    @Override
    public synchronized boolean authorize(Action action, HttpRequest request, Person person) {

        String deviceName = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

        String authId = UUID.randomUUID().toString();
        System.out.println("Authorizing " + deviceName + "(" + authId + ")");

        for (Session session : Persons.getActiveSessions(person)) {

            System.out.println("Checking session " + session.getSessionToken() + " for " + authId);

            if (!Sessions.isLive(session)) {
                System.out.println("Skipping session, it is not alive");
                continue;
            }

            System.out.println("Session is alive!");

            Station station = session.getStation();
            System.out.println("Station for session is " + station.getName() + " for " + authId);

            // System.out.println("A");
            for (Device device : station.getDevices()) {

                // System.out.println("B");
                System.out.println("Checking device " + device.getName() + " in station for " + authId);

                if (device.getName().equals(deviceName)) {
                    System.out.println("authorized!");
                    return true;
                }
            }
        }

        System.out.println("unauthorized");
        return false;

    }

}
