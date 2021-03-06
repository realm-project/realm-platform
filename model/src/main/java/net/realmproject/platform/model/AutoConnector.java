package net.realmproject.platform.model;


import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.w3c.dom.Document;

import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.Parameter;
import net.objectof.connector.sql.AbstractSQLConnector;
import net.objectof.connector.sql.IH2Connector;
import net.objectof.connector.sql.ISQLiteConnector;
import net.objectof.model.Package;
import net.objectof.model.Transaction;


public class AutoConnector implements Connector {

    private Connector backer;
    private String schemaFile = "/platform-schema.xml";
    private String contentsFile = "/platform-db.json";
    private String dbName = null;

    public AutoConnector(String appname, Connector backer) {
        this.backer = backer;
    }

    public AutoConnector(String appname) {
        backer = new ISQLiteConnector();

        String dbdir = System.getProperty("user.home") + "/.realm/applications/" + appname + "/";
        String dbfile = dbdir + appname + ".db";

        backer.getParameter(ISQLiteConnector.KEY_FILENAME).setValue(dbfile);
        backer.getParameter(IH2Connector.KEY_REPOSITORY).setValue("realmproject.net:1502/realm");
    }

    public AutoConnector(Connector backer) {
        this.backer = backer;
    }

    private void init() throws ConnectorException {

        // create repository first
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaFile);
        createPackage(schemaStream, Initialize.WHEN_EMPTY);

        // populate it
        if (contentsFile == null) { return; }
        InputStream contentsStream = getClass().getClassLoader().getResourceAsStream(contentsFile);
        Transaction loaderTx = getPackage().connect(AutoConnector.class.getName());
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(contentsStream);
        scanner = scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String str = scanner.next();
            // skip blank lines including the one at the end of the file
            if (str.trim().length() == 0) {
                continue;
            }
            StringReader reader = new StringReader(str);
            loaderTx.receive("application/json", reader);
        }
        loaderTx.post();
        scanner.close();

    }

    public Package getPackage() throws ConnectorException {
        String name = getParameter(AbstractSQLConnector.KEY_REPOSITORY).getValue();

        if (!hasPackage(name)) {
            init();
        }

        return backer.getPackage();

    }

    public String getSchema() {
        return schemaFile;
    }

    public void setSchema(String schemaFile) {
        this.schemaFile = schemaFile;
    }

    public String getContents() {
        return contentsFile;
    }

    public void setContents(String contentsFile) {
        this.contentsFile = contentsFile;
    }

    public Package createPackage(Document schema, Initialize initialize) throws ConnectorException {
        return backer.createPackage(schema, initialize);
    }

    public Package createPackage(InputStream schema, Initialize initialize) throws ConnectorException {
        return backer.createPackage(schema, initialize);
    }

    public String getPackageName() {
        return backer.getPackageName();
    }

    public boolean isContainerEmpty() throws ConnectorException {
        return backer.isContainerEmpty();
    }

    public void initializeContainer() throws ConnectorException {
        backer.initializeContainer();
    }

    public List<String> getPackageNames() throws ConnectorException {
        return backer.getPackageNames();
    }

    public List<Parameter> getParameters() {
        return backer.getParameters();
    }

    public Parameter getParameter(String name) {
        return backer.getParameter(name);
    }

    public void setParameter(String parameter, String value) {
        backer.setParameter(parameter, value);
    }

    public void setParameters(Map<String, String> values) {
        backer.setParameters(values);
    }

    public String getName() {
        return backer.getName();
    }

    public void setName(String name) {
        backer.setName(name);
    }

    public String getType() {
        return backer.getType();
    }

    public boolean hasPackage(String name) throws ConnectorException {
        return backer.hasPackage(name);
    }

}
