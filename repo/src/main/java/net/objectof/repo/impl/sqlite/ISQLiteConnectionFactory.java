package net.objectof.repo.impl.sqlite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp2.ConnectionFactory;

class ISQLiteConnectionFactory implements ConnectionFactory {

	private Connection conn;

	public ISQLiteConnectionFactory(File db) throws IOException, SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Driver 'org.sqlite.JDBC' not installed.");
		}

		boolean newdb = !db.exists();

		conn = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
		conn.setAutoCommit(false);
		conn = new ISingleConnectionDecorator(conn);

		if (newdb) {
			populate();
		}

	}

	public Connection createConnection() throws SQLException {
		return conn;
	}

	private void populate() throws SQLException, IOException {
		ISQLiteScriptRunner script;
		InputStream sqlStream;
		Reader reader;

		script = new ISQLiteScriptRunner(conn, true);
		sqlStream = ISQLite.class.getResourceAsStream("/net/objectof/repo/res/sqlite/repo.sql");
		reader = new InputStreamReader(sqlStream);
		script.runScript(reader);

		script = new ISQLiteScriptRunner(conn, true);
		sqlStream = ISQLite.class.getResourceAsStream("/net/objectof/repo/res/sqlite/rip.sql");
		reader = new InputStreamReader(sqlStream);
		script.runScript(reader);

	}

}