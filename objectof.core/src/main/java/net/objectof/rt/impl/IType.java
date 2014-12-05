package net.objectof.rt.impl;

import net.objectof.InvalidNameException;
import net.objectof.Type;

public abstract class IType extends IFn implements Type
{
  public static final Class<?> fromName(String aComponentName)
      throws InvalidNameException
  {
    try
    {
      return Class.forName(aComponentName);
    }
    catch (ClassNotFoundException e)
    {
      throw new InvalidNameException(aComponentName);
    }
  }

  public IType()
  {
  }

  @Override
  public String getUniqueName()
  {
    return runtime().getUniqueName() + '/' + peer().getName();
  }

  @Override
  public boolean isInstance(Object aObject)
  {
    return peer().isInstance(aObject);
  }

  public abstract Class<?> peer();

  @Override
  public String toString()
  {
    return "ans://" + getUniqueName();
  }
}
