package net.objectof.connector.impl;


import javax.sql.DataSource;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.parameter.Parameter.Type;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISql;
import net.objectof.repo.impl.sql.ISqlDb;

import org.w3c.dom.Document;


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
    public Package getPackage() {
        return getDb().forName(getPackageName());
    }

    @Override
    public Package createPackage(Document schema) {
        ISqlDb db = getDb();
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        Package repo = db.createPackage(getPackageName(), IRip.class.getName(), schemaPackage);
        return repo;
    }

    private ISqlDb getDb() {
        String serverString = "jdbc:postgresql://" + value(KEY_SERVER) + "/" + value(KEY_DATABASE);
        DataSource ds = ISql
                .createPool(serverString, value(KEY_USERNAME), value(KEY_PASSWORD), "org.postgresql.Driver");
        return new ISqlDb("net/objectof/repo/res/postgres/statements", ds);
    }

    @Override
    public String getType() {
        return "Postgres";
    }
}
