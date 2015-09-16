package net.realmproject.platform.util.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.objectof.model.Id;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Station;


public class Stations {

    /**
     * Returns a list of all people who own or share access to all
     * {@link Device}s in a station.
     * 
     * @param station
     *            the station to report on
     * @return a list of {@link Person}s
     */
    public static Set<Person> getSharers(Station station) {

        // WHY HAS OBJECTOF OBJECT EQUALITY STOPPED WORKING???
        Set<Id<?>> owners = new HashSet<>();
        Set<Id<?>> other = new HashSet<>();

        List<Device> devices = new ArrayList<>(station.getDevices().values());
        if (devices.size() == 0) { return owners.stream()
                .map(id -> (Person) station.tx().retrieve(id))
                .collect(Collectors.toSet()); }

        owners.addAll(devices.get(0).getSharers().stream().map(Person::id).collect(Collectors.toSet()));
        owners.add(devices.get(0).getOwner().id());

        for (Device device : devices) {
            other.clear();
            other.add(device.getOwner().id());
            other.addAll(device.getSharers().stream().map(Person::id).collect(Collectors.toSet()));

            owners.retainAll(other);
        }

        Set<Person> ownerPersons = owners.stream()
                .map(id -> (Person) station.tx().retrieve(id))
                .collect(Collectors.toSet());

        return ownerPersons;

    }

    /**
     * Checks to see if the given {@link Person} owns or shares all
     * {@link Device}s in this station
     * 
     * @param person
     *            the person to report on
     * @param station
     *            the station to report on
     * @return true if this person owns or shares all devices in this station,
     *         false otherwise
     */
    public static boolean isSharer(Person person, Station station) {
        for (Person p : getSharers(station)) {
            if (p.id().equals(person.id())) { return true; }
        }
        return false;
    }

}
