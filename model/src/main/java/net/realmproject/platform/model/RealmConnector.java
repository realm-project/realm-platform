package net.realmproject.platform.model;


import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import net.objectof.connector.AbstractConnector;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.impl.ISQLiteConnector;
import net.objectof.model.Package;
import net.objectof.model.Transaction;


public class RealmConnector extends ISQLiteConnector {

    private String schemaFile, contentsFile;

    public RealmConnector(Map<String, String> values, String schemaFile, String contentsFile) {
        super(values);
        this.schemaFile = schemaFile;
        this.contentsFile = contentsFile;
    }

    private void init() throws ConnectorException {

        // create repository first
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaFile);
        createPackage(schemaStream, Initialize.WHEN_EMPTY);

        // populate it
        InputStream contentsStream = getClass().getClassLoader().getResourceAsStream(contentsFile);
        Transaction loaderTx = getPackage().connect(RealmConnector.class.getName());
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(contentsStream);
        scanner = scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String str = scanner.next();
            StringReader reader = new StringReader(str);
            loaderTx.receive("application/json", reader);
        }
        scanner.close();

    }

    public Package getPackage() throws ConnectorException {
        String name = getParameter(AbstractConnector.KEY_REPOSITORY).getValue();

        if (!hasRepository(name)) {
            init();
        }

        return super.getPackage();

    }

}
