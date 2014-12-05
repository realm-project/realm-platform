package net.objectof.aggr.impl;

import net.objectof.ext.Vector;
import net.objectof.rt.Interface;
import net.objectof.rt.impl.IFn;

public class IAggregateKeyParameter extends IFn implements Vector<Interface>
{
  private final Interface theType;

  public IAggregateKeyParameter(Class<?> aClass)
  {
    theType = runtime().forClass(aClass);
  }

  public IAggregateKeyParameter(Interface aType)
  {
    theType = aType;
  }

  @Override
  public Interface get(int aIndex)
  {
    if (aIndex != 0)
    {
      throw new IndexOutOfBoundsException();
    }
    return theType;
  }

  @Override
  public int size()
  {
    return 1;
  }

  @Override
  public Interface type()
  {
    return IAggregateInterface.newInstance(runtime().forClass(getClass()),
        runtime().forClass(Integer.class), runtime().forClass(Interface.class));
  }
}
