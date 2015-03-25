package net.objectof.repo.impl.sqlite;


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;


class ISingleConnectionDecorator implements Connection {

    Connection backer;

    public ISingleConnectionDecorator(Connection backer) throws SQLException {
        this.backer = backer;
        backer.setAutoCommit(false);
    }

    public void abort(Executor executor) throws SQLException {
        backer.abort(executor);
    }

    public void clearWarnings() throws SQLException {
        backer.clearWarnings();
    }

    public void close() throws SQLException {
        // backer.close();
    }

    public void commit() throws SQLException {
        backer.commit();
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return backer.createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException {
        return backer.createBlob();
    }

    public Clob createClob() throws SQLException {
        return backer.createClob();
    }

    public NClob createNClob() throws SQLException {
        return backer.createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return backer.createSQLXML();
    }

    public Statement createStatement() throws SQLException {
        return backer.createStatement();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return backer.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return backer.createStatement(resultSetType, resultSetConcurrency);
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return backer.createStruct(typeName, attributes);
    }

    public boolean getAutoCommit() throws SQLException {
        return backer.getAutoCommit();
    }

    public String getCatalog() throws SQLException {
        return backer.getCatalog();
    }

    public Properties getClientInfo() throws SQLException {
        return backer.getClientInfo();
    }

    public String getClientInfo(String name) throws SQLException {
        return backer.getClientInfo(name);
    }

    public int getHoldability() throws SQLException {
        return backer.getHoldability();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return backer.getMetaData();
    }

    public int getNetworkTimeout() throws SQLException {
        return backer.getNetworkTimeout();
    }

    public String getSchema() throws SQLException {
        return backer.getSchema();
    }

    public int getTransactionIsolation() throws SQLException {
        return backer.getTransactionIsolation();
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return backer.getTypeMap();
    }

    public SQLWarning getWarnings() throws SQLException {
        return backer.getWarnings();
    }

    public boolean isClosed() throws SQLException {
        return backer.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        return backer.isReadOnly();
    }

    public boolean isValid(int timeout) throws SQLException {
        return backer.isValid(timeout);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return backer.isWrapperFor(iface);
    }

    public String nativeSQL(String sql) throws SQLException {
        return backer.nativeSQL(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return backer.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return backer.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return backer.prepareCall(sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) throws SQLException {
        return backer.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return backer.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return backer.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return backer.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return backer.prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return backer.prepareStatement(sql);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        backer.releaseSavepoint(savepoint);
    }

    public void rollback() throws SQLException {
        backer.rollback();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        backer.rollback(savepoint);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        // backer.setAutoCommit(autoCommit);
    }

    public void setCatalog(String catalog) throws SQLException {
        backer.setCatalog(catalog);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        backer.setClientInfo(properties);
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        backer.setClientInfo(name, value);
    }

    public void setHoldability(int holdability) throws SQLException {
        backer.setHoldability(holdability);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        backer.setNetworkTimeout(executor, milliseconds);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        // TODO: Disabled to update from SQLite 3.7.2 to 3.8.7, as it does not
        // seem to support changing the read-only state after making a
        // connection
        // backer.setReadOnly(readOnly);
    }

    public Savepoint setSavepoint() throws SQLException {
        return backer.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return backer.setSavepoint(name);
    }

    public void setSchema(String schema) throws SQLException {
        backer.setSchema(schema);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        backer.setTransactionIsolation(level);
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        backer.setTypeMap(map);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return backer.unwrap(iface);
    }
}
