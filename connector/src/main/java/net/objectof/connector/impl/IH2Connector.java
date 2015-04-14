package net.objectof.connector.impl;


import java.io.File;

import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.repo.impl.sql.ISql;


public class IH2Connector extends AbstractConnector {

    private static final String KEY_DIRECTORY = "Directory";

    public IH2Connector() {
        super();
        addParameter(Type.DIRECTORY, KEY_DIRECTORY);
    }

    @Override
    public String getType() {
        return "H2";
    }

    @Override
    protected DataSource getDataSource() throws ConnectorException {
        String prefixPath = value(KEY_DIRECTORY) + "/" + "Database";
        String serverString = "jdbc:h2:" + prefixPath;
        return ISql.createPool(serverString, null, null, "org.h2.Driver");
    }

    @Override
    protected boolean isDatabaseCreatable() {
        File dbfile = new File(value(KEY_DIRECTORY) + "/Database.mv.db");
        return !dbfile.exists();
    }
}
