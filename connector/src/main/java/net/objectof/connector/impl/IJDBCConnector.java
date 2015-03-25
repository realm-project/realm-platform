package net.objectof.connector.impl;


import java.sql.SQLException;

import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISql;
import net.objectof.repo.impl.sql.ISqlDb;

import org.w3c.dom.Document;


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

    private ISqlDb getDb() throws ClassNotFoundException, SQLException {
        DataSource ds = ISql.createPool(value(KEY_URL), value(KEY_USERNAME), value(KEY_PASSWORD), value(KEY_DRIVER));
        return new ISqlDb("net/objectof/repo/res/postgres/statements", ds);
    }

    @Override
    public Package getPackage() throws ConnectorException {
        try {
            return getDb().forName(getPackageName());
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public Package createPackage(Document schema) throws ConnectorException {
        try {
            ISqlDb db = getDb();
            IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
            Package repo = db.createPackage(getPackageName(), IRip.class.getName(), schemaPackage);
            return repo;
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public String getType() {
        return "Generic JDBC";
    }
}
