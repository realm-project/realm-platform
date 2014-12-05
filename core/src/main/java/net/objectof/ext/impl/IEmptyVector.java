package net.objectof.ext.impl;

import java.util.Collections;
import java.util.Iterator;

import net.objectof.ext.Vector;

public class IEmptyVector<V> implements Vector<V>, Iterable<V>
{
  private static final Vector<?> INSTANCE = new IEmptyVector<>();

  public static final <T> Vector<T> emptyInstance()
  {
    @SuppressWarnings("unchecked")
    Vector<T> ret = (Vector<T>) INSTANCE;
    return ret;
  }

  @Override
  public V get(int aIndex)
  {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public Iterator<V> iterator()
  {
    return Collections.emptyIterator();
  }

  @Override
  public int size()
  {
    return 0;
  }
}
