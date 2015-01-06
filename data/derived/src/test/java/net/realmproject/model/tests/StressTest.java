package net.realmproject.model.tests;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IIndexed;
import net.realmproject.model.schema.DeviceCommand;
import net.realmproject.model.schema.DeviceIO;

public class StressTest {
	
	public final static String DEVICE_DATA = "{\"endEffectorWidth\": 1000,\"JointAngles\": [1001,1002,1003,1004,1005,1006],\"Pose\": {\"Point\": [1001,1002,1003],\"Quaternion\": [1001,1002,1003,1004]},\"PID\": [1001.0,1002.0,1003.0]}";
//	public final static SimpleDateFormat FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss.SSS");
	
	public static void main(String[] args) {
		
		try {
			int recordNo = 25000;
		
			System.out.println("recordNo: " + recordNo);
			
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, before TestRealmRepo.repo()");
		
			Package repo = TestRealmRepo.repo();
		
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after TestRealmRepo.repo() and before repo.connect");
		
			Transaction t = repo.connect("Azade");
		
//			Create DeviceCommands
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after repo.connect and before createADeviceCommand loop");
			
			for (int i = 0; i < recordNo; i ++)
				createADeviceCommand(t);

			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after createADeviceCommand loop");
		
////		Retriveng DeviceCommands
//		long startRetrieving = Calendar.getInstance().getTimeInMillis();
//		System.out.println("Start retriveng " + recordNo + " DeviceCommands ...");
//		for (int i = 1; i <= recordNo; i ++)
//			retrieveDeviceCommand(t, Integer.toString(i));
//		long endRetrieving = Calendar.getInstance().getTimeInMillis();
//		System.out.println("DeviceCommands are created! Total time: " + (endRetrieving - startRetrieving) + " milliseconds. Start posting the transaction ...");

		
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, before t.post");
		
			t.post();
		
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after t.post and before t.close");
		
			t.close();
		
			System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after t.close");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void retrieveDeviceCommand(Transaction t, String label) {
		
			System.out.println(">>>>> DeviceCommand" + "-" + label);
			
			DeviceCommand deviceCommand = (DeviceCommand) t.retrieve("DeviceCommand", label);
			PrintObject.print(t, deviceCommand);		
			
			DeviceIO command = deviceCommand.getCommand();
			if (command != null) {
				System.out.print("command: ");
				PrintObject.print(t, command);
			}
			
			IIndexed<DeviceIO> states = (IIndexed<DeviceIO>) deviceCommand.getStates();
			
			if (states != null) {
				System.out.println("states: ");

				for (DeviceIO deviceIO : states) {
					if (deviceIO != null)
						PrintObject.print(t, deviceIO);
				}
			}
			
		
	}
	
	private static DeviceCommand createADeviceCommand(Transaction t) {
		DeviceCommand deviceCommand = (DeviceCommand) t.create("DeviceCommand");
		
		deviceCommand.setCommand(createADeviceIO(t));
		
		@SuppressWarnings("unchecked")
		IIndexed<DeviceIO> states = (IIndexed<DeviceIO>) t.create("DeviceCommand.states");
		
		for (int i = 0; i < 10; i ++) {
			states.add(createADeviceIO(t));	
		}
		
		deviceCommand.setStates(states);
		
		return deviceCommand;
	}
	
	private static DeviceIO createADeviceIO(Transaction t) {
		
		DeviceIO deviceIO = (DeviceIO) t.create("DeviceIO");
		deviceIO.setJson(DEVICE_DATA);
		deviceIO.setUnixtime(Instant.now().getEpochSecond());
		
		return deviceIO;
	} 
}
