package net.realmproject.model.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.objectof.aggr.Listing;
import net.objectof.aggr.Mapping;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.objectof.repo.impl.sql.ISqlDb;
import net.realmproject.model.schema.Device;
import net.realmproject.model.schema.DeviceCommand;
import net.realmproject.model.schema.DeviceIO;
import net.realmproject.model.schema.Session;

public class TestSessionDeviceData {

	private final static String ACTOR = "Azade";
	final static String REPO_NAME = "realmproject.net:1408/dev2";

	private static Package repo() {
		ISqlDb db = new ISqlDb("realmDatabase");
		return db.getPackage(REPO_NAME);
	}

	public static void main(String[] args) {
		Transaction tx = repo().connect(ACTOR);

//		listDeviceIOs(tx);
		
		listDeviceCommandStates();
		System.out.println("+++++++++++++");
		createMultipleDeviceCommandStates();
		System.out.println("+++++++++++++");
		listDeviceCommandStates();
		
//		Listing<DeviceIO> states = createDeviceCommandStates(tx);
//		try {
//			CreateRepo.print(tx, states);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		DeviceCommand deviceCommand = createDeviceCommand(tx);
//		
//		addState(deviceCommand, tx);
//		
//		tx.post();
//		
//		try {
//			CreateRepo.print(tx, deviceCommand);
//			CreateRepo.print(tx, deviceCommand.getCommand());
//			Listing<DeviceIO> states = deviceCommand.getStates();
//			for (DeviceIO state : states)
//				CreateRepo.print(tx, state);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		Object object = tx.retrieve("SessionDeviceData", "1");
//		
//		Listing<DeviceCommand> commands = ((SessionDeviceData)object).getCommands();
//
//		try {
//			CreateRepo.print(tx, object);
//
//			for (DeviceCommand c : commands) {
//				CreateRepo.print(tx, c);
//				System.out.println("command: ");
//				CreateRepo.print(tx, c.getCommand());
//				System.out.println("states: ");
//				Listing<DeviceIO> states = c.getStates();
//				for (DeviceIO state : states)
//					CreateRepo.print(tx, state);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private static void listDeviceIOs(Transaction tx) {
		for (int i = 1; i < 30; i ++)
			PrintObject.print(tx, tx.retrieve("DeviceIO", Integer.toString(i)));
	}
	
	private static void listDeviceCommandStates() {
		Transaction tx = repo().connect("Azade");
		
		for (int i = 1; i < 30; i ++)
			PrintObject.print(tx, tx.retrieve("DeviceCommand.states", Integer.toString(i)));
	}
	
	private static void createMultipleDeviceCommandStates() {
		for (int i = 1; i <= 10; i ++) {
			createDeviceCommandStates(Integer.toString(i));
		}
	}
	
	private static Listing<DeviceIO> createDeviceCommandStates(String deviceIOLabel) {
		Transaction tx = repo().connect(ACTOR);
		Listing<DeviceIO> states = tx.retrieve("DeviceCommand.states", deviceIOLabel);
//		System.out.println("Adding state to " + ((Resource)states).id().getUniqueName());
//		Listing<DeviceIO> states = tx.create("DeviceCommand.states");
//		DeviceIO state = tx.create("DeviceIO");
		DeviceIO state = tx.retrieve("DeviceIO", deviceIOLabel);
		state.setJson("JSON " + deviceIOLabel + deviceIOLabel);
		state.setUnixtime(new Date().getTime());
		states.add(state);
		tx.post();
		return states;
	}
	
	private static DeviceCommand createDeviceCommand(Transaction tx) {
		DeviceCommand deviceCommand = tx.create("DeviceCommand");
		
		DeviceIO command = tx.create("DeviceIO");
		command.setJson("aaa");
		command.setUnixtime(new Date().getTime());
		
		deviceCommand.setCommand(command);
		
		Listing<DeviceIO> states = tx.create("DeviceCommand.states");
		DeviceIO state = tx.create("DeviceIO");
		state.setJson("bbb");
		state.setUnixtime(new Date().getTime());
		states.add(state);
		
		deviceCommand.setStates(states);
		
		return deviceCommand;
	}
	
	private static void addState(DeviceCommand deviceCommand, Transaction tx) {
		DeviceIO state = tx.create("DeviceIO");
		
		state.setJson("ccc");
		state.setUnixtime(new Date().getTime());
		
		deviceCommand.getStates().add(state);
	}
}
