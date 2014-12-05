package net.objectof.ext.impl;

import net.objectof.ext.Vector;

public class IVector<T> implements Vector<T>
{
  private final T[] theElements;

  @SafeVarargs
  public IVector(T... aElements)
  {
    theElements = aElements;
  }

  @Override
  public T get(int aIndex)
  {
    return theElements[aIndex];
  }

  @Override
  public int size()
  {
    return theElements.length;
  }
}
