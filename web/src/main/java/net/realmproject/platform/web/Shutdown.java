package net.realmproject.platform.web;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.realmproject.dcm.util.DCMThreadPool;


@WebListener
public class Shutdown implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {}

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DCMThreadPool.stop();
    }

}