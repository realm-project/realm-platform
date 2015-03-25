package net.objectof.connector;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Connectors {

    private static Log log = LogFactory.getLog(Connectors.class);

    public static List<Connector> getConnectors() {
        List<Connector> connectors = new ArrayList<>();
        try {
            connectors.add(new ISQLiteConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load SQLite Connector", e);
        }
        try {
            connectors.add(new IPostgresConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load Postgres Connector", e);
        }
        return connectors;
    }

    public static Connector getConnectorByType(String name) {
        for (Connector conn : getConnectors()) {
            if (conn.getType().equals(name)) { return conn; }
        }
        return null;
    }
}
