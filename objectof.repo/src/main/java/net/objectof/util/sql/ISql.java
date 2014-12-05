package net.objectof.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class ISql
{
  public static final void close(Connection aConnection)
  {
    if (aConnection == null)
    {
      return;
    }
    try
    {
      aConnection.close();
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }

  public static void close(ResultSet aRs)
  {
    if (aRs == null)
    {
      return;
    }
    try
    {
      aRs.close();
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }

  public static void close(Statement aStmt)
  {
    if (aStmt == null)
    {
      return;
    }
    try
    {
      aStmt.close();
    }
    catch (Exception e)
    {
      System.out.println(e);
    }
  }

  public static DataSource createPool(String aName)
  {
    ResourceBundle bundle = ResourceBundle.getBundle(aName);
    String password = bundle.getString("PWD");
    String principal = bundle.getString("PRINCIPAL");
    String conn = bundle.getString("CONNECTION");
    DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
        conn, principal, password);
    PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
        connectionFactory, null);
    ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(
        poolableConnectionFactory);
    poolableConnectionFactory.setPool(connectionPool);
    PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(
        connectionPool);
    return dataSource;
  }

  public final static PreparedStatement prepare(Connection aConnection,
      String aBundleName, String aStatementName) throws SQLException
  {
    String sql = ResourceBundle.getBundle(aBundleName)
        .getString(aStatementName);
    return aConnection.prepareStatement(sql);
  }
}
