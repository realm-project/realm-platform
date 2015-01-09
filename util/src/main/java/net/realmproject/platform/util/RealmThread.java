package net.realmproject.platform.util;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;


public class RealmThread {

    static private Log log = RealmLog.getLog();
    static ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

    public static ScheduledExecutorService getThreadPool() {
        return pool;
    }

    public static void shutdown() {

        pool.shutdown();

        try {
            pool.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            log.error("ThreadPool Shutdown Interrupted", e);
        }

        pool.shutdownNow();

        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            log.error("ThreadPool ShutdownNow Interrupted", e);
        }

    }

}
