package net.objectof.connector.testing;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import net.objectof.connector.ConnectorException;
import net.objectof.connector.ISQLiteConnector;
import net.objectof.model.Package;


public class TempSQLiteRepo {

    public static Package testPackage() throws URISyntaxException, IOException, ConnectorException {
        return forSchema(TEST_SCHEMA);
    }

    public static String TEST_SCHEMA = "/packages/test.xml";

    public static Package forSchema(String relativeFilename) throws URISyntaxException, IOException, ConnectorException {
        URL schemaURL = TempSQLiteRepo.class.getResource(relativeFilename);
        File schemaFile = new File(schemaURL.toURI());
        return forSchema(schemaFile);
    }

    public static Package forSchema(File schemaFile) throws IOException, ConnectorException {
        File dbFile = File.createTempFile("testdb", "");
        dbFile.delete();
        ISQLiteConnector conn = new ISQLiteConnector();
        conn.getParameter("Filename").setValue(dbFile.toString());
        conn.getParameter("Version").setValue("1");
        conn.getParameter("Repository").setValue("testrepo");
        dbFile.deleteOnExit();
        return conn.createPackage(new FileInputStream(schemaFile));
    }
}
