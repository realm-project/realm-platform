package net.objectof.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IConnectionEmitter extends IConnector
{
  private final String theName;
  private final String thePassword;
  private final String theDriver;
  private final String theInstance;
  private final String thePrincipal;

  public IConnectionEmitter(String aName)
  {
    ResourceBundle props = ResourceBundle.getBundle(aName);
    theName = aName;
    thePassword = props.getString("PWD");
    theDriver = props.getString("DRIVER");
    theInstance = props.getString("CONNECTION");
    thePrincipal = props.getString("PRINCIPAL");
  }

  @Override
  public Connection getConnection()
  {
    return newConnection();
  }

  public Connection newConnection()
  {
    try
    {
      Class.forName(theDriver);
      Connection c = DriverManager.getConnection(theInstance, thePrincipal,
          thePassword);
      c.setAutoCommit(false);
      return c;
    }
    catch (ClassNotFoundException ex)
    {
      throw new RuntimeException("Cannot locate JDBC driver: "
          + ex.getMessage(), ex);
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String toString()
  {
    return theName;
  }
}
