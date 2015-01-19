package net.objectof.repo.impl.sqlite;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

//Modified from http://www.codeproject.com/Articles/802383/Run-SQL-Script-sql-containing-DDL-DML-SELECT-state
class ISQLiteScriptRunner {

    public static final String DEFAULT_DELIMITER = ";";
    public static final String PL_SQL_BLOCK_SPLIT_DELIMITER = "+";
    public static final String PL_SQL_BLOCK_END_DELIMITER = "#";

    private final boolean stopOnError;
    private final Connection connection;
    private String delimiter = ISQLiteScriptRunner.DEFAULT_DELIMITER;
    private final PrintWriter out, err;


    public ISQLiteScriptRunner(final Connection connection, final boolean stopOnError) {
        if (connection == null) {
            throw new RuntimeException("ScriptRunner requires an SQL Connection");
        }
		
        this.connection = connection;
        this.stopOnError = stopOnError;
        this.out = new PrintWriter(System.out);
        this.err = new PrintWriter(System.err);

    }

	public void runScript(final Reader reader) throws SQLException, IOException {
		this.runScript(this.connection, reader);
	}

    private void runScript(final Connection conn, final Reader reader) throws SQLException, IOException {
        StringBuffer command = null;

        try {
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }

                String trimmedLine = line.trim();
                trimmedLine = trimmedLine.replaceAll("[^\\x00-\\x7f]", "");

				// Interpret SQL Comment & Some statement that are not executable
                if (trimmedLine.startsWith("--")
                        || trimmedLine.startsWith("//")
                        || trimmedLine.startsWith("#")
                        || trimmedLine.toLowerCase().startsWith("rem inserting into")
                        || trimmedLine.toLowerCase().startsWith("set define off")) {

                    // do nothing...
                } else if (trimmedLine.endsWith(this.delimiter) || trimmedLine.endsWith(PL_SQL_BLOCK_END_DELIMITER)) { // Line is end of statement
                    
                    // Append
                    if (trimmedLine.endsWith(this.delimiter)) {
                        command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
                        command.append(" ");
                    } else if (trimmedLine.endsWith(PL_SQL_BLOCK_END_DELIMITER)) {
                        command.append(line.substring(0, line.lastIndexOf(PL_SQL_BLOCK_END_DELIMITER)));
                        command.append(" ");
                    }

                    Statement stmt = null;
                    try {
                        stmt = conn.createStatement();
                        if (this.stopOnError) {
                            stmt.execute(command.toString());
                        } else {
                            try {
                                stmt.execute(command.toString());
                            } catch (final SQLException e) {
                                e.fillInStackTrace();
                                err.println("Error executing SQL Command: \"" + command + "\"");
                                err.println(e);
                                err.flush();
                                throw e;
                            }
                        }

                        
                        command = null;
                    } finally {
                        if (stmt != null) {
                            try {
                                stmt.close();
                            } catch (final Exception e) {
                                err.println("Failed to close statement: " + e.getMessage());
                                err.flush();
                            }
                        }
                    }
                } else if (trimmedLine.endsWith(PL_SQL_BLOCK_SPLIT_DELIMITER)) {
                    command.append(line.substring(0, line.lastIndexOf(this.PL_SQL_BLOCK_SPLIT_DELIMITER)));
                    command.append(" ");
                } else { // Line is middle of a statement

                    // Append
                    command.append(line);
                    command.append(" ");
                }
            }
           
            conn.commit();
            
        } catch (final SQLException e) {
            conn.rollback();
            e.fillInStackTrace();
            err.println("Error executing SQL Command: \"" + command + "\"");
            err.println(e);
            err.flush();
            throw e;
        } catch (final IOException e) {
            e.fillInStackTrace();
            err.println("Error reading SQL Script.");
            err.println(e);
            err.flush();
            throw e;
        }
    }

}