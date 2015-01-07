package net.realmproject.model.tests;


import static org.junit.Assert.assertTrue;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IMapped;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Station;
import net.realmproject.model.utils.PrintObject;
import net.realmproject.service.data.model.RealmSchema;

import org.junit.Test;


public class JUnitTest {

    @Test
    public void isMappedNull() throws Exception {

        Transaction t = RealmSchema.getTestPackage().connect(this);

        Station st = t.create("Station");

        t.post();

        assertTrue("Station.devices should be null.", st.getDevices() == null);

        t.close();
    }

    @Test
    public void testIndexOfMapped() throws Exception {
        Transaction t = RealmSchema.getTestPackage().connect("");
        int j = 0;

        IMapped<Device> devices = t.create("Station.devices");
        t.create("Station.devices");

        for (int i = 0; i < 10; i++) {
            Device device = t.create("Device");
            device.setName("Device name " + i);
            devices.put("device" + i, device);
        }

        t.post();

        try {
            for (Device device : devices) {
                PrintObject.print(t, device);

                // CreateRepo.print(t, devices.get("device" +
                // Integer.toString(j)));
                //
                // if (device.equals(devices.get("device" + Integer.toString(j
                // ++))))
                // System.out.println("true;");

                assertTrue(device.equals(devices.get("device" + Integer.toString(j++))));
            }
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        t.close();
    }

    // public static void main(String[] args) {
    // testIndexOfIndexed();
    // }
}
