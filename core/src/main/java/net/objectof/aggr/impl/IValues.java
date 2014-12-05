package net.objectof.aggr.impl;

import java.util.Iterator;

import net.objectof.aggr.Aggregate;

public class IValues<K, V> implements Iterator<V>
{
  private final Aggregate<K, V> theAggregate;
  private final Iterator<K> theIterator;

  public IValues(Aggregate<K, V> aAggregate)
  {
    theAggregate = aAggregate;
    theIterator = aAggregate.keySet().iterator();
  }

  @Override
  public boolean hasNext()
  {
    return theIterator.hasNext();
  }

  @Override
  public V next()
  {
    return theAggregate.get(theIterator.next());
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
