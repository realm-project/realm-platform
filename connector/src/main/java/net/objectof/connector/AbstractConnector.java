package net.objectof.connector;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.objectof.connector.Parameter.Hint;
import net.objectof.model.Package;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;
import net.objectof.repo.impl.sqlite.ISQLite;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public abstract class AbstractConnector implements Connector {

    public static final String KEY_REPOSITORY = "Repository";
    private String name = "";
    private List<Parameter> parameters = new ArrayList<>();

    public AbstractConnector() {
        addParameter(KEY_REPOSITORY);
    }

    @Override
    public String getPackageName() {
        return value(KEY_REPOSITORY);
    }

    public boolean isDatabaseEmpty() throws ConnectorException {
        if (isDatabaseCreatable()) { return true; }
        try {
            Connection conn = getDataSource().getConnection();
            ResultSet res = conn.getMetaData().getTables(null, null, null, null);
            while (res.next()) {
                String type = res.getString("TABLE_TYPE");
                if (!type.equals("SYSTEM TABLE")) {
                    res.close();
                    conn.close();
                    return false;
                }
            }
            res.close();
            conn.close();
            return true;
        }
        catch (SQLException e) {
            throw new ConnectorException(e);
        }
    }

    public List<String> getSchemaNames() throws ConnectorException {
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
        isDatabaseEmpty();
        return getISqlDb().forName(getPackageName());
    }

    @Override
    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        IPackage schemaPackage = new ISourcePackage(IBaseMetamodel.INSTANCE, schema);
        if (initialize == Initialize.WHEN_EMPTY && isDatabaseEmpty()) {
            initializeDatabase();
        }
        return getISqlDb().createPackage(getPackageName(), IRip.class.getName(), schemaPackage);
    }

    @Override
    public void initializeDatabase() throws ConnectorException {
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

    protected void addParameter(Parameter param) {
        parameters.add(param);
    }

    protected void addParameter(String title) {
        addParameter(new Parameter(title));
    }

    protected void addParameter(String title, Hint hint) {
        addParameter(new Parameter(title, hint));
    }

    protected String value(String key) {
        return getParameter(key).getValue();
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
    public String toString() {
        return getName();
    }

    @Override
    public List<Parameter> getParameters() {
        return new ArrayList<>(parameters);
    }

    @Override
    public Parameter getParameter(String name) {
        for (Parameter p : parameters) {
            if (name.equals(p.getTitle())) { return p; }
        }
        return null;
    }

    public void setParameter(String parameter, String value) {
        Parameter p = getParameter(parameter);
        if (p == null) { return; }
        p.setValue(value);
    }

    public void setParameters(Map<String, String> values) {
        for (String parameter : values.keySet()) {
            setParameter(parameter, values.get(parameter));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
