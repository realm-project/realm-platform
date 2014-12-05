package net.objectof.rt;

import net.objectof.Context;

public interface Runtime<T extends Interface> extends Context<T>
{
  public T forClass(Class<?> aClass);

  public String getLocation(String aUniformName);

  public String getPackageName(String aUniformName);

  public T forInterfaceName(String aUniqueName);
}
