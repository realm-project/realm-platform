package net.realmproject.model.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IMapped;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.Session;

import org.junit.Test;

public class JUnitTest {
	
	protected Package repo()
	  {
	    ISqlDb db = new ISqlDb("realmDatabase");
	    return db.getPackage("realmproject.net:1408/master");
	  }

	@Test
	public void isMappedNull() {
		
		Transaction t = repo().connect(this);
		
		Session session = t.create("Session");
		
		t.post();

		assertTrue("Session.devices should be null.", session.getDevices() == null);
		
		t.close();
	}
	
	@Test
	public void testIndexOfMapped() {
		Transaction t = repo().connect("");
		int j = 0;
		
		IMapped<Device> devices = t.create("Session.devices");
		t.create("Session.devices");

		for (int i = 0; i < 10; i++)
		{
			Device device = t.<Resource<Device>>create("Device").value();
			device.setName("Device name " + i);
			devices.put("device" + i, device);
		}
		
		t.post();
		
		try {
			for (Device device : devices) {
				PrintObject.print(t, device);
				
//				CreateRepo.print(t, devices.get("device" + Integer.toString(j)));
//				
//				if (device.equals(devices.get("device" + Integer.toString(j ++))))
//					System.out.println("true;");
				
				assertTrue(device.equals(devices.get("device" + Integer.toString(j ++))));
			}
		}		
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
		
		t.close();
	}
	
//	public static void main(String[] args) {
//		testIndexOfIndexed(); 
//	}
}
