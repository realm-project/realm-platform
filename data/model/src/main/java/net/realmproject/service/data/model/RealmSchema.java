package net.realmproject.service.data.model;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;

import net.objectof.actof.connector.SQLiteConnector;
import net.objectof.model.Package;

import org.apache.commons.io.IOUtils;


public class RealmSchema {

    private static File schemafile;

    public static File get() {
        try {

            if (schemafile == null) {
                InputStream stream = RealmSchema.class.getResourceAsStream("/packages/realm.xml");
                schemafile = File.createTempFile("realm-schema-", ".xml");
                Writer writer = new FileWriter(schemafile);
                writer.write(IOUtils.toString(stream));
                writer.close();
            }

            return schemafile;

        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Package getTestPackage() throws Exception {

        URL schemaURL = RealmSchema.class.getResource("/packages/realm.xml");
        File schemaFile = new File(schemaURL.toURI());
        File dbFile = File.createTempFile("realmtestdb", "");
        dbFile.delete();

        SQLiteConnector conn = new SQLiteConnector();
        conn.getParameter("Filename").setValue(dbFile.toString());
        conn.getParameter("Version").setValue("1502");
        conn.getParameter("Repository").setValue("testrepo");

        dbFile.deleteOnExit();

        return conn.createPackage(new FileInputStream(schemaFile));

    }

}
