package net.objectof.corc;

public interface Interface<T>
{
  public Class<? extends Action> getActionClass();

  public Class<? extends T> getArgumentClass();

  public Class<? extends Object> getReturnClass();
}
