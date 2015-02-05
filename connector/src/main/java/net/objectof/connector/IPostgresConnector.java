package net.objectof.connector;


import javax.sql.DataSource;

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

    public IPostgresConnector() {
        super();
        addParameter(Type.STRING, "Server");
        addParameter(Type.STRING, "Database");
        addParameter(Type.STRING, "Username");
        addParameter(Type.PASSWORD, "Password");
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
        String serverString = "jdbc:postgresql://" + value("Server") + "/" + value("Database");
        DataSource ds = ISql.createPool(serverString, value("Username"), value("Password"), "org.postgresql.Driver");
        return new ISqlDb("net/objectof/repo/res/postgres/statements", ds);
    }

    @Override
    public String getPackageName() {
        return value("Domain") + ":" + value("Version") + "/" + value("Repository");
    }

    @Override
    public String getType() {
        return "Postgres";
    }
}
