package net.objectof.aggr.impl;

import java.util.AbstractCollection;

import net.objectof.aggr.Aggregate;
import net.objectof.ext.Dominion;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IFn;
import net.objectof.rt.impl.IInterface;

public abstract class IAggregate<K, V> extends AbstractCollection<V> implements
    Aggregate<K, V>, Instance
{
  private final IInterface theType;

  protected IAggregate(Class<K> aKeyClass, Class<V> aValueClass)
  {
    theType = initType(runtime(), getClass(), runtime().forClass(aKeyClass),
        runtime().forClass(aValueClass));
  }

  protected IAggregate(IInterface aType)
  {
    theType = aType;
  }

  protected IAggregate(Interface aKeyType, Interface aValueType)
  {
    theType = initType(runtime(), getClass(), aKeyType, aValueType);
  }

  protected IAggregate(Runtime<? extends IInterface> aRuntime,
      Interface aKeyType, Interface aValueType)
  {
    theType = initType(aRuntime, getClass(), aKeyType, aValueType);
  }

  @Override
  public Instance apply(Object... aMessage)
  {
    if (aMessage.length == 0)
    {
      return this;
    }
    throw new IllegalArgumentException("Extraneous arguments to apply().");
  }

  public final long count()
  {
    return size();
  }

  @Override
  public Object evaluate(Object... aMessage)
  {
    return get(aMessage[0]);
  }

  public abstract boolean isAlterable();

  @Override
  public Object perform(String aSelector, Object... aMessage)
  {
    return theType.dispatch(aSelector, this, aMessage);
  }

  @Override
  public Instance select(String aSelector)
  {
    return theType.create(aSelector, this);
  }

  @Override
  public final Interface type()
  {
    return theType;
  }

  protected <T> T find(String aUniqueName)
  {
    @SuppressWarnings("unchecked")
    T object = (T) Dominion.INSTANCE.find(aUniqueName);
    return object;
  }

  protected IInterface initType(Runtime<? extends IInterface> aRuntime,
      Class<?> aClass, Interface aKeyType, Interface aValueType)
  {
    return IAggregateInterface.newInstance(aRuntime.forClass(aClass), aKeyType,
        aValueType);
  }

  /**
   * Override to provide different type implementations.
   */
  protected Runtime<? extends IInterface> runtime()
  {
    if (theType == null)
    {
      return find(IFn.DEFAULT_RUNTIME);
    }
    return theType.runtime();
  }
}
