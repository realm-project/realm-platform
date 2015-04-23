package net.objectof.connector.sql;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter.Hint;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.sqlite.ISQLite;

import org.w3c.dom.Document;


public class ISQLiteConnector extends AbstractSQLConnector {

    public static final String KEY_FILENAME = "Filename";

    public ISQLiteConnector(Map<String, String> values) {
        this();
        setParameters(values);
    }

    public ISQLiteConnector() {
        super();
        addParameter(KEY_FILENAME, Hint.FILE);
    }

    @Override
    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        if (initialize == Initialize.WHEN_EMPTY && isContainerEmpty()) {
            initializeContainer();
        }
        return getISqlDb().createPackage(getPackageName(), ISQLite.class.getName(), schemaPackage);
    }

    @Override
    protected DataSource getDataSource() throws ConnectorException {
        File file = new File(value(KEY_FILENAME));
        DataSource ds;
        try {
            ds = ISQLite.createPool(file);
        }
        catch (IOException | SQLException e) {
            throw new ConnectorException(e);
        }
        return ds;
    }

    @Override
    public String getType() {
        return "SQLite";
    }

    @Override
    protected boolean isDatabaseCreatable() {
        return !new File(value(KEY_FILENAME)).exists();
    }
}
