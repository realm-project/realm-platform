package net.objectof.connector.impl;


import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.repo.impl.sql.ISql;


public class IJDBCConnector extends AbstractConnector {

    private static final String KEY_DRIVER = "Driver";
    private static final String KEY_URL = "URL";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_PASSWORD = "Password";

    public IJDBCConnector() {
        super();
        addParameter(Type.STRING, KEY_DRIVER);
        addParameter(Type.STRING, KEY_URL);
        addParameter(Type.STRING, KEY_USERNAME);
        addParameter(Type.PASSWORD, KEY_PASSWORD);
    }

    @Override
    public String getType() {
        return "Generic JDBC";
    }

	@Override
	protected DataSource getDataSource() throws ConnectorException {
		return ISql.createPool(value(KEY_URL), value(KEY_USERNAME), value(KEY_PASSWORD), value(KEY_DRIVER));
	}
}
