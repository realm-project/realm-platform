package net.objectof.aggr.impl;

import java.util.ArrayList;

import net.objectof.aggr.Listing;
import net.objectof.aggr.Set;
import net.objectof.ext.Dominion;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IFn;
import net.objectof.rt.impl.IInterface;

public class IListing<V> extends ArrayList<V> implements Listing<V>, Instance
{
  private static final long serialVersionUID = 1L;
  private final IInterface theType;

  public IListing(Class<?> aContainedClass)
  {
    super();
    theType = IAggregateInterface.newInstance(runtime().forClass(getClass()),
        Integer.class, aContainedClass);
  }

  public IListing(Interface aContainedType)
  {
    theType = IAggregateInterface.newInstance(runtime().forClass(getClass()),
        runtime().forClass(Integer.class), aContainedType);
  }

  protected IListing(IInterface aType)
  {
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
    if (aMessage.length == 0)
    {
      return this;
    }
    throw new IllegalArgumentException("Extraneous arguments to evaluate().");
  }

  @Override
  public V get(Object aKey)
  {
    int key = (Integer) aKey;
    // Don't throw an error if the aKey isn't in a valid range as per aggregate
    // contract.
    return key < 0 || key >= size() ? null : get((int) aKey);
  }

  public boolean isAlterable()
  {
    return true;
  }

  @Override
  public Set<Integer> keySet()
  {
    return new IIntRange(0, size());
  }

  @Override
  public Object perform(String aSelector, Object... aMessage)
  {
    return type().dispatch(aSelector, this, aMessage);
  }

  @Override
  public Instance select(String aSelector)
  {
    return type().create(aSelector, this);
  }

  @Override
  public V set(Integer aKey, V aValue)
  {
    int end = aKey;
    // TODO review ensure capacity -- doesn't seem to work.
    for (int i = size(); i <= end; i++)
    {
      add(null);
    }
    return set((int) aKey, aValue);
  }

  @Override
  public final IInterface type()
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
