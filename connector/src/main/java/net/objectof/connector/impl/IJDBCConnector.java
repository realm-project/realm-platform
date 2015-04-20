package net.objectof.connector.impl;


import java.util.Map;

import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter.Hint;
import net.objectof.repo.impl.sql.ISql;


public class IJDBCConnector extends AbstractConnector {

    private static final String KEY_DRIVER = "Driver";
    private static final String KEY_URL = "URL";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_PASSWORD = "Password";

    public IJDBCConnector(Map<String, String> values) {
        this();
        setParameters(values);
    }

    public IJDBCConnector() {
        super();
        addParameter(KEY_DRIVER);
        addParameter(KEY_URL);
        addParameter(KEY_USERNAME);
        addParameter(KEY_PASSWORD, Hint.PASSWORD);
    }

    @Override
    public String getType() {
        return "Generic JDBC";
    }

    @Override
    protected DataSource getDataSource() throws ConnectorException {
        return ISql.createPool(value(KEY_URL), value(KEY_USERNAME), value(KEY_PASSWORD), value(KEY_DRIVER));
    }

    protected boolean isDatabaseCreatable() {
        return false;
    }
}
