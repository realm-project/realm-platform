package net.realmproject.platform.util.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.objectof.model.Transaction;
import net.realmproject.platform.schema.Device;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Station;


public class Stations {

    public static Set<Person> getSharers(Transaction tx, Station station) {

        Set<Person> owners = new HashSet<>();
        Set<Person> other = new HashSet<>();

        List<Device> devices = new ArrayList<>(station.getDevices().values());
        if (devices.size() == 0) { return owners; }

        owners.addAll(devices.get(0).getSharers());
        owners.add(devices.get(0).getOwner());

        for (Device device : devices) {
            other.clear();
            other.add(device.getOwner());
            other.addAll(device.getSharers());
            owners.retainAll(other);
        }

        return owners;

    }

}
