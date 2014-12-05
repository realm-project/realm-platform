package net.objectof.util.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.objectof.repo.impl.sql.ISql;

public class IConnectionPool extends IConnector
{
  private final String theName;
  private final DataSource theConnections;

  public IConnectionPool(String aName)
  {
    theName = aName;
    theConnections = ISql.createPool(aName);
  }

  @Override
  public Connection getConnection()
  {
    try
    {
      Connection conn = theConnections.getConnection();
      conn.setAutoCommit(false);
      return conn;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public String getName()
  {
    return theName;
  }
}
