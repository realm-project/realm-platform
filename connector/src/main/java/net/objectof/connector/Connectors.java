package net.objectof.connector;


import java.util.ArrayList;
import java.util.List;


public class Connectors {

    public static List<Connector> getConnectors() {
        List<Connector> connectors = new ArrayList<>();

        connectors.add(new SQLiteConnector());
        connectors.add(new PostgresConnector());

        return connectors;
    }

    public static Connector getConnectorByType(String name) {
        for (Connector conn : getConnectors()) {
            if (conn.getType().equals(name)) { return conn; }
        }
        return null;
    }

}
