package net.objectof.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import net.objectof.rt.impl.base.Ex;

public class IStatements
{
  private final ResourceBundle theStatements;

  public IStatements(String aName)
  {
    theStatements = ResourceBundle.getBundle(aName);
  }

  public String getStatement(String aKey)
  {
    return theStatements.getString(aKey);
  }

  public PreparedStatement prepare(Connection aConnection, String aStatementKey)
  {
    String sql = getStatement(aStatementKey);
    try
    {
      return aConnection.prepareStatement(sql);
    }
    catch (SQLException ex)
    {
      throw new Ex(aStatementKey, ex);
    }
  }
}
