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

public class StressTestMultipleTxs {
	
		public final static String DEVICE_DATA = "{\"endEffectorWidth\": 1000,\"JointAngles\": [1001,1002,1003,1004,1005,1006],\"Pose\": {\"Point\": [1001,1002,1003],\"Quaternion\": [1001,1002,1003,1004]},\"PID\": [1001.0,1002.0,1003.0]}";
//		public final static SimpleDateFormat FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss.SSS");
		
		public static void main(String[] args) {
			
			try {
				Package repo = TestRealmRepo.repo();
				int recordNo = 55000;

				System.out.println("recordNo: " + recordNo);

//				Create DeviceCommands
				System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after repo.connect and before createADeviceCommand loop");

				for (int i = 0; i < recordNo; i ++) {
//					System.out.println("i: " + i);
					createADeviceCommand(repo);
				}

				System.out.println((new SimpleDateFormat("d MMM yyyy HH:mm:ss.SSS")).format(Calendar.getInstance().getTimeInMillis()) + " - main, after createADeviceCommand loop");

//				retrieveDeviceCommand(repo, "638228");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private static void retrieveDeviceCommand(Package repo, String label) {

			Transaction t = repo.connect("Azade");

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


			t.post();
			t.close();
		}
		
		private static DeviceCommand createADeviceCommand(Package repo) {

			Transaction t = null;
			DeviceCommand deviceCommand = null;

			try {
				t = repo.connect("Azade");
				deviceCommand = t.create("DeviceCommand");

				deviceCommand.setCommand(createADeviceIO(t));

				IIndexed<DeviceIO> states = t.create("DeviceCommand.states");

				for (int i = 0; i < 10; i ++) {
					states.add(createADeviceIO(t));	
				}

				deviceCommand.setStates(states);
				
//				CreateRepo.print(t, deviceCommand);
			} 
			catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				if (t != null) {
					t.post();
					t.close();
				}
			}
			return deviceCommand;
		}
		
		private static DeviceIO createADeviceIO(Transaction t) throws IOException {
			
			DeviceIO deviceIO = t.create("DeviceIO");
			deviceIO.setJson(DEVICE_DATA);
			deviceIO.setUnixtime(new Date().getTime());
//			CreateRepo.print(t, deviceIO);
			
			return deviceIO;
		} 
	}
