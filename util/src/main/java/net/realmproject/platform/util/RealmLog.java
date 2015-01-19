package net.realmproject.platform.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RealmLog {

    // private static Logger logger = Logger.getLogger("realmproject");

    // private static Log log = new Log4JLogger(logger);
    private static Log log = LogFactory.getLog(RealmLog.class);

    // public static Log getLog() {
    // return log;
    // }

    // public static void configLog(String logfile) throws IOException {
    // logfile = "." + logfile + ".log";
    // System.out.println("Writing RealmProject logs to " + logfile);
    // logger.setLevel(Level.DEBUG);
    // logger.addAppender(new FileAppender(new PatternLayout("%-5p [%d]: %m%n"),
    // logfile));
    // }

}
