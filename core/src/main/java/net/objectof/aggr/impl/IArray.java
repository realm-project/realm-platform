package net.objectof.aggr.impl;

import java.lang.reflect.Array;
import java.util.Iterator;

public class IArray<T> extends IVector<T>
{
  private final T[] theArray;

  public IArray(Class<T> aClass, int aSize)
  {
    super(aClass);
    @SuppressWarnings("unchecked")
    T[] newInstance = (T[]) Array.newInstance(aClass, aSize);
    theArray = newInstance;
  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  public IArray(T... aArray)
  {
    super((Class<T>) aArray.getClass().getComponentType());
    theArray = aArray;
  }

  @Override
  public T get(int aIndex) throws IndexOutOfBoundsException
  {
    return theArray[aIndex];
  }

  @Override
  public T get(Object aKey)
  {
    return theArray[(Integer) aKey];
  }

  @Override
  public boolean isAlterable()
  {
    return true;
  }

  @Override
  public Iterator<T> iterator()
  {
    return new IArrayIterator<T>(theArray);
  }

  @Override
  public final T set(int aIndex, T aValue)
  {
    if (!isAlterable())
    {
      throw new IllegalStateException();
    }
    if (!type().getEvaluation().isInstance(aValue))
    {
      throw new IllegalArgumentException("Value must be a '"
          + type().getEvaluation());
    }
    T prior = theArray[aIndex];
    theArray[aIndex] = aValue;
    return prior;
  }

  @Override
  public int size()
  {
    return theArray.length;
  }
}
