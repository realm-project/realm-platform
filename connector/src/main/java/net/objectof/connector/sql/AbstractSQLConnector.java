package net.objectof.connector.sql;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter;
import net.objectof.connector.SQLScriptRunner;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;
import net.objectof.repo.impl.sqlite.ISQLite;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public abstract class AbstractSQLConnector extends AbstractConnector implements Connector {

    public static final String KEY_REPOSITORY = "Repository";

    public AbstractSQLConnector() {
        addParameter(KEY_REPOSITORY);
    }

    @Override
    public String getPackageName() {
        return value(KEY_REPOSITORY);
    }

    public boolean isContainerEmpty() throws ConnectorException {
        if (isDatabaseCreatable()) { return true; }
        List<String> allowedTypes = new ArrayList<>(Arrays.asList("SYSTEM TABLE", "SYSTEM INDEX", "SYSTEM TOAST INDEX",
                "SYSTEM VIEW"));
        try {
            Connection conn = getDataSource().getConnection();
            ResultSet res = conn.getMetaData().getTables(null, null, null, null);
            while (res.next()) {
                String type = res.getString("TABLE_TYPE");
                if (allowedTypes.contains(type)) {
                    continue;
                }
                if (type == null) {
                    continue; // TODO: Why is this happening in postgres?
                }
                res.close();
                conn.close();
                return false;
            }
            res.close();
            conn.close();
            return true;
        }
        catch (SQLException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public boolean hasPackage(String name) throws ConnectorException {
        return getPackageNames().contains(name);
    }

    @Override
    public List<String> getPackageNames() throws ConnectorException {
        Connection conn = null;
        List<String> names = new ArrayList<>();
        if (isDatabaseCreatable()) { return names; }
        try {
            conn = getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("select chars.chars from repositories, chars where chars.id = repositories.uniform_name_txt");
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                names.add(rs.getString("chars"));
            }
            rs.close();
            return names;
        }
        catch (SQLException | ConnectorException e) {
            throw new ConnectorException(e);
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Package getPackage() throws ConnectorException {
        return getISqlDb().forName(getPackageName());
    }

    @Override
    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        if (initialize == Initialize.WHEN_EMPTY && isContainerEmpty()) {
            initializeContainer();
        }
        return getISqlDb().createPackage(getPackageName(), IRip.class.getName(), schemaPackage);
    }

    @Override
    public void initializeContainer() throws ConnectorException {
        SQLScriptRunner script;
        InputStream sqlStream;
        Reader reader;
        Connection conn = null;
        try {
            conn = getDataSource().getConnection();
            conn.setReadOnly(false);
            String type = getType().toLowerCase().replace(" ", "-");
            script = new SQLScriptRunner(conn, true);
            sqlStream = ISQLite.class.getResourceAsStream("/net/objectof/repo/res/" + type + "/repo.sql");
            reader = new InputStreamReader(sqlStream);
            script.runScript(reader);
            script = new SQLScriptRunner(conn, true);
            sqlStream = ISQLite.class.getResourceAsStream("/net/objectof/repo/res/" + type + "/rip.sql");
            reader = new InputStreamReader(sqlStream);
            script.runScript(reader);
        }
        catch (SQLException | IOException e) {
            throw new ConnectorException(e);
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e) {}
            }
        }
    }

    /**
     * Returns a DataSource for this type of Connector. If this database is
     * creatable (Connector{@link #isDatabaseCreatable()} is true, getDataSource
     * should attempt to create it
     * 
     * @return a DataSource for this Connector
     * @throws ConnectorException
     */
    protected abstract DataSource getDataSource() throws ConnectorException;

    /**
     * Checks to see if the configured database is yet to be created. This only
     * applies to lightweight databases like SQLite which can create new
     * databases on the fly.
     * 
     * @return true if the database can, but has yet to be, created, false
     *         otherwise
     */
    protected abstract boolean isDatabaseCreatable();

    protected ISqlDb getISqlDb() throws ConnectorException {
        return new ISqlDb("net/objectof/repo/res/postgres/statements", getDataSource());
    }

    @Override
    public Package createPackage(InputStream schema, Initialize initialize) throws ConnectorException {
        try {
            return createPackage(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(schema), initialize);
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            throw new ConnectorException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!Connector.class.isAssignableFrom(o.getClass())) { return false; }
        Connector other = (Connector) o;
        if (!other.getName().equals(getName())) { return false; }
        if (!other.getType().equals(getType())) { return false; }
        if (!other.getPackageName().equals(getPackageName())) { return false; }
        for (Parameter p : getParameters()) {
            Parameter op = other.getParameter(p.getTitle());
            if (!p.getValue().equals(op.getValue())) { return false; }
        }
        return true;
    }
}
