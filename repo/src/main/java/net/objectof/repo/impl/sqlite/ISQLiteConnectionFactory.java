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

	/**
	 * Manages connections to a SQLite database. This is required because 
	 * SQLite does not share access when using 
	 * {@link Connection#TRANSACTION_SERIALIZABLE} as we do. In reality, 
	 * this connection factory returns the same connection for each request
	 * @param db The location of an initialized SQLite objectof repository 
	 *        database
	 * @throws IOException
	 * @throws SQLException
	 */
	public ISQLiteConnectionFactory(File db) throws IOException, SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Driver 'org.sqlite.JDBC' not installed.");
		}

		conn = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
		conn.setAutoCommit(false);
		conn = new ISingleConnectionDecorator(conn);

	}

	public Connection createConnection() throws SQLException {
		return conn;
	}

}
