package net.objectof.repo.impl.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import net.objectof.model.RepositoryException;

import org.apache.commons.dbcp2.ConnectionFactory;
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
    String conn = bundle.getString("connection");
    String principal = bundle.getString("user");
    String password = bundle.getString("password");
    String driver = bundle.getString("driver");
    
    return createPool(conn, principal, password, driver);
    
  }

  public static DataSource createPool(String conn, String principal, String password, String driver) {
	  try
	    {
	      Class.forName(driver);
	    }
	    catch (ClassNotFoundException e)
	    {
	      throw new RuntimeException("Driver '" + driver + "' not installed.");
	    }
	    DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
	        conn, principal, password);
	    
	    return createPool(connectionFactory);
	    
  }
  
  public static DataSource createPool(ConnectionFactory connectionFactory) {
	  return createPool(connectionFactory, Connection.TRANSACTION_READ_COMMITTED);
  }
  
  public static DataSource createPool(ConnectionFactory connectionFactory, int isolation) {
	  
	  
	  
	    PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
	        connectionFactory, null);
	    poolableConnectionFactory.setMaxConnLifetimeMillis(3000);
	    poolableConnectionFactory.setDefaultAutoCommit(false);
	    poolableConnectionFactory.setDefaultReadOnly(true);
	    poolableConnectionFactory
	        .setDefaultTransactionIsolation(isolation);
	    ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(
	        poolableConnectionFactory);
	    poolableConnectionFactory.setPool(connectionPool);
	    PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(
	        connectionPool);
	    return dataSource;
  }
  
  public final static PreparedStatement prepare(Connection aConnection,
		  String sql)
  {
    try
    {
      return aConnection.prepareStatement(sql);
    }
    catch (SQLException e)
    {
      throw new RepositoryException(sql, e);
    }
  }
  
  public final static PreparedStatement prepare(Connection aConnection,
      ResourceBundle aBundle, String aStatementName)
  {
    try
    {
      String sql = aBundle.getString(aStatementName);
      return aConnection.prepareStatement(sql);
    }
    catch (SQLException e)
    {
      throw new RepositoryException(aStatementName, e);
    }
  }
}
