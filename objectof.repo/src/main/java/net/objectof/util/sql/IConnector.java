package net.objectof.util.sql;

import java.sql.Connection;

public abstract class IConnector
{
  public abstract Connection getConnection();
}
