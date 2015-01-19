package net.objectof.repo.impl.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.objectof.model.Transaction;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISql;
import net.objectof.repo.impl.sql.ISqlDb;

public class ISQLite extends IRip {

	private final static Map<String, DataSource> datasources = new HashMap<>();
	
	public ISQLite(ISqlDb aDb, String aName) {
		super(aDb, aName);
	}

	@Override
	public Transaction connect(Object aActor) {
		/*
		 * TODO we need to actually create the transaction in the database as
		 * multiple concurrent repositories creating transactions will fail.
		 * What we really need is a global database object.
		 */
		Transaction tx = super.connect(aActor);
		tx = new ISingleConnectionTxDecorator(tx, ISQLite.class);
		return tx;
	}
	
	public static DataSource createPool(File dbFile) throws IOException, SQLException {
		String filename = dbFile.getAbsolutePath();
		DataSource ds;
		if (!datasources.containsKey(filename)) {
			ISQLiteConnectionFactory factory = new ISQLiteConnectionFactory(dbFile);
			ds = ISql.createPool(factory, Connection.TRANSACTION_SERIALIZABLE);
			datasources.put(filename, ds);
		}
		
		return datasources.get(filename);
	}

}
