package net.objectof.connector;


import java.util.ArrayList;
import java.util.List;

import net.objectof.connector.impl.IH2Connector;
import net.objectof.connector.impl.IJDBCConnector;
import net.objectof.connector.impl.IMySQLConnector;
import net.objectof.connector.impl.IPostgresConnector;
import net.objectof.connector.impl.ISQLiteConnector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Connectors {

    private static Log log = LogFactory.getLog(Connectors.class);

    public static List<Connector> getConnectors() {
        List<Connector> connectors = new ArrayList<>();
        // SQLite
        try {
            connectors.add(new ISQLiteConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load SQLite Connector", e);
        }
        // MySQL
        try {
            connectors.add(new IMySQLConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load MySQL Connector", e);
        }
        // PostgreSQL
        try {
            connectors.add(new IPostgresConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load Postgres Connector", e);
        }
        // Generic JDBC
        try {
            connectors.add(new IH2Connector());
        }
        catch (Exception e) {
            log.warn("Failed to load H2 Connector", e);
        }
        // Generic JDBC
        try {
            connectors.add(new IJDBCConnector());
        }
        catch (Exception e) {
            log.warn("Failed to load JDBC Connector", e);
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
