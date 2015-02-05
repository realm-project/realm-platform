package net.objectof.connector;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.sql.ISqlDb;
import net.objectof.repo.impl.sqlite.ISQLite;

import org.w3c.dom.Document;


public class ISQLiteConnector extends AbstractConnector {

    public ISQLiteConnector(Map<String, String> values) {
        this();
        setParameters(values);
    }

    public ISQLiteConnector() {
        super();
        addParameter(Type.FILE, "Filename");
    }

    @Override
    public Package getPackage() throws ConnectorException {
        return getDb().forName(getPackageName());
    }

    @Override
    public Package createPackage(Document schema) throws ConnectorException {
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        return getDb().createPackage(getPackageName(), ISQLite.class.getName(), schemaPackage);
    }

    private ISqlDb getDb() throws ConnectorException {
        try {
            DataSource ds = ISQLite.createPool(new File(value("Filename")));
            ISqlDb db = new ISqlDb("net/objectof/repo/res/postgres/statements", ds);
            return db;
        }
        catch (SQLException | IOException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public String getPackageName() {
        return value("Domain") + ":" + value("Version") + "/" + value("Repository");
    }

    @Override
    public String getType() {
        return "SQLite";
    }
}
