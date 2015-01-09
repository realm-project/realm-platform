package net.realmproject.platform.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class RealmLog {
	
	private static Logger logger = Logger.getLogger("realmproject");
	private static Log log = new Log4JLogger(logger);
	
	public static Log getLog() {
		return log;
	}
	
	public static void configLog(String logfile) throws IOException {
		logfile = "." + logfile + ".log";
		System.out.println("Writing RealmProject logs to " + logfile);
		logger.setLevel(Level.DEBUG);
		logger.addAppender(new FileAppender(new PatternLayout("%-5p [%d]: %m%n"), logfile));
	}


}
