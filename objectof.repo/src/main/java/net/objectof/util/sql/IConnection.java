package net.objectof.util.sql;

import java.sql.Connection;

public class IConnection extends IConnector
{
  private final Connection theConnection;

  public IConnection(Connection aConnection)
  {
    theConnection = aConnection;
  }

  @Override
  public Connection getConnection()
  {
    return theConnection;
  }
}
