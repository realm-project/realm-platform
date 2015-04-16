package net.objectof.connector.impl;


import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter.Hint;
import net.objectof.repo.impl.sql.ISql;


public class IMySQLConnector extends AbstractConnector {

    private static final String KEY_SERVER = "Server";
    private static final String KEY_DATABASE = "Database";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_PASSWORD = "Password";

    public IMySQLConnector() {
        super();
        addParameter(KEY_SERVER);
        addParameter(KEY_DATABASE);
        addParameter(KEY_USERNAME);
        addParameter(KEY_PASSWORD, Hint.PASSWORD);
    }

    @Override
    public String getType() {
        return "MySQL";
    }

    @Override
    protected DataSource getDataSource() throws ConnectorException {
        String serverString = "jdbc:mysql://" + value(KEY_SERVER) + "/" + value(KEY_DATABASE);
        return ISql.createPool(serverString, value(KEY_USERNAME), value(KEY_PASSWORD), "com.mysql.jdbc.Driver");
    }

    protected boolean isDatabaseCreatable() {
        return false;
    }
}
