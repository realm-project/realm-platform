package net.objectof.aggr.impl;

import java.util.Iterator;

public class IArrayIterator<T> implements Iterator<T>
{
  private int theCurrent;
  private final T[] theValues;

  @SafeVarargs
  public IArrayIterator(T... aValues)
  {
    theValues = aValues;
  }

  @Override
  public boolean hasNext()
  {
    return theCurrent < theValues.length;
  }

  @Override
  public T next()
  {
    return theValues[theCurrent++];
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
