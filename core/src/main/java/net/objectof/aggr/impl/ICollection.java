package net.objectof.aggr.impl;

import java.util.Collection;
import java.util.Iterator;

import net.objectof.aggr.Set;
import net.objectof.rt.Interface;
import net.objectof.rt.impl.IInterface;

public abstract class ICollection<K, V> extends IAggregate<K, V>
{
  private final Collection<V> theCollection;

  public ICollection(Class<K> aKeyClass, Class<V> aValueClass,
      Collection<V> aCollection)
  {
    super(aKeyClass, aValueClass);
    theCollection = aCollection;
  }

  public ICollection(Interface aKeyType, Interface aValueType,
      Collection<V> aCollection)
  {
    super(aKeyType, aValueType);
    theCollection = aCollection;
  }

  protected ICollection(IInterface aType, Collection<V> aCollection)
  {
    super(aType);
    theCollection = aCollection;
  }

  @Override
  public abstract V get(Object aKey);

  @Override
  public boolean isAlterable()
  {
    return false;
  }

  @Override
  public Iterator<V> iterator()
  {
    return theCollection.iterator();
  }

  @Override
  public abstract Set<K> keySet();

  @Override
  public V set(K aKey, V aValue)
  {
    throw new IllegalStateException();
  }

  @Override
  public int size()
  {
    return theCollection.size();
  }
}