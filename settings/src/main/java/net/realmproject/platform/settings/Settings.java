package net.realmproject.platform.settings;


import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import net.objectof.connector.Connector;
import net.objectof.connector.Connector.Initialize;
import net.objectof.connector.ConnectorException;
import net.objectof.connector.sql.ISQLiteConnector;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;


public class Settings {

    Package store;

    public Settings(String appname) throws ConnectorException {
        String name = "realmproject.net:1519/settings";
        Connector connector = new ISQLiteConnector();

        String dbdir = System.getProperty("user.home") + "/.realm/applications/" + appname + "/";
        String dbfile = dbdir + "settings.db";
        new File(dbdir).mkdirs();

        connector.setParameter(ISQLiteConnector.KEY_REPOSITORY, "realmproject.net:1519/settings");
        connector.setParameter(ISQLiteConnector.KEY_FILENAME, dbfile);

        if (!connector.hasPackage(name)) {
            InputStream schema = Settings.class.getResourceAsStream("/packages/settings-schema.xml");
            store = connector.createPackage(schema, Initialize.WHEN_EMPTY);
        } else {
            store = connector.getPackage();
        }
    }

    public void put(String key, String value) {
        Setting setting = getSetting(key);
        if (setting == null) {
            setting = createSetting(key);
        }
        setting.setValue(value);
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String fallback) {
        Setting setting = getSetting(key);
        if (setting == null) { return fallback; }
        return setting.getValue();
    }

    private Setting getSetting(String key) {
        Transaction tx = store.connect(Settings.class.getName());
        Iterable<Setting> settings = tx.query("Setting", new IQuery("key", key));
        Iterator<Setting> settingsIter = settings.iterator();
        if (!settingsIter.hasNext()) { return null; }
        Setting setting = settingsIter.next();
        return setting;
    }

    private Setting createSetting(String key) {
        Transaction tx = store.connect(Settings.class.getName());
        Setting setting = tx.create("Setting");
        setting.setKey(key);
        tx.post();
        return getSetting(key);
    }

}
