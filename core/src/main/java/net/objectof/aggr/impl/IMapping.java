package net.objectof.aggr.impl;

import java.util.HashMap;
import java.util.Iterator;

import net.objectof.Type;
import net.objectof.aggr.Mapping;
import net.objectof.aggr.Set;
import net.objectof.ext.Dominion;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IFn;
import net.objectof.rt.impl.IInterface;

public class IMapping<K, V> extends HashMap<K, V> implements Mapping<K, V>,
Instance
{
  private static final long serialVersionUID = 1L;
  private final IInterface theType;
  private Set<K> theKeys;

  public IMapping(Class<?> aKeyClass, Class<?> aValueClass)
  {
    super();
    theType = IAggregateInterface.newInstance(runtime().forClass(getClass()),
        aKeyClass, aValueClass);
  }

  public IMapping(Interface aKeyType, Interface aValueType)
  {
    super();
    theType = IAggregateInterface.newInstance(runtime().forClass(getClass()),
        aKeyType, aValueType);
  }

  protected IMapping(IInterface aType, int aInitCap, float aLoadFactor)
  {
    super(aInitCap, aLoadFactor);
    theType = aType;
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

  public long count()
  {
    return size();
  }

  @Override
  public Object evaluate(Object... aMessage)
  {
    return get(aMessage[0]);
  }

  public Type getElementType()
  {
    return null;
  }

  public boolean isAlterable()
  {
    return true;
  }

  @Override
  public Iterator<V> iterator()
  {
    return values().iterator();
  }

  @Override
  public Set<K> keySet()
  {
    Set<K> keys = theKeys;
    if (keys == null)
    {
      synchronized (this)
      {
        if (keys == null) // race conditions.
        {
          keys = new ISet<K>(type().getParameters().get(0), super.keySet());
          theKeys = keys;
        }
      }
    }
    return keys;
  }

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
  public V set(K aKey, V aValue)
  {
    if (!isAlterable())
    {
      throw new IllegalStateException();
    }
    return put(aKey, aValue);
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

  /**
   * Override to provide different type implementations.
   */
  protected Runtime<? extends IInterface> runtime()
  {
    return find(IFn.DEFAULT_RUNTIME);
  }
}
