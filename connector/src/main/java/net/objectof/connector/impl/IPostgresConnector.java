package net.objectof.connector.impl;


import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.repo.impl.sql.ISql;


public class IPostgresConnector extends AbstractConnector {

    private static final String KEY_SERVER = "Server";
    private static final String KEY_DATABASE = "Database";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_PASSWORD = "Password";

    public IPostgresConnector() {
        super();
        addParameter(Type.STRING, KEY_SERVER);
        addParameter(Type.STRING, KEY_DATABASE);
        addParameter(Type.STRING, KEY_USERNAME);
        addParameter(Type.PASSWORD, KEY_PASSWORD);
    }

    @Override
    public String getType() {
        return "Postgres";
    }

    @Override
    protected DataSource getDataSource() throws ConnectorException {
        String serverString = "jdbc:postgresql://" + value(KEY_SERVER) + "/" + value(KEY_DATABASE);
        return ISql.createPool(serverString, value(KEY_USERNAME), value(KEY_PASSWORD), "org.postgresql.Driver");
    }

    protected boolean isDatabaseCreatable() {
        return false;
    }
}
