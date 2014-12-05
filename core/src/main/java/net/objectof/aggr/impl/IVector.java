package net.objectof.aggr.impl;

import net.objectof.aggr.Set;
import net.objectof.ext.Vector;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IInterface;

public abstract class IVector<T> extends IAggregate<Integer, T> implements
    Vector<T>
{
  private static final Vector<Interface> PARAMETERS = new IAggregateKeyParameter(
      Integer.class);

  public IVector(Class<T> aValueClass)
  {
    super(Integer.class, aValueClass);
  }

  public IVector(Interface aValueType)
  {
    super(null, aValueType);
  }

  @Override
  public abstract T get(int aIndex) throws IndexOutOfBoundsException;

  @Override
  public T get(Object aKey)
  {
    return get((int) aKey);
  }

  @Override
  public Set<Integer> keySet()
  {
    return new IIntRange(0, size());
  }

  public abstract T set(int aIndex, T aValue);

  @Override
  public T set(Integer aKey, T aValue)
  {
    return set((int) aKey, aValue);
  }

  @Override
  protected IInterface initType(Runtime<? extends IInterface> aRuntime,
      Class<?> aClass, Interface aKeyType, Interface aValueType)
  {
    if (aKeyType != null && aKeyType != aRuntime.forClass(Integer.class))
    {
      throw new RuntimeException("Invalid Vector type.");
    }
    return new IAggregateInterface(aRuntime.forClass(aClass), PARAMETERS,
        aValueType);
  }
}
