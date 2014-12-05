package net.objectof.ext.impl;

import java.util.Collections;
import java.util.Iterator;

import net.objectof.ext.View.Viewset;

public class IEmptyView<T> implements Viewset<T>
{
  private static final Viewset<?> EMPTY = new IEmptyView<>();

  public static final <V> Viewset<V> emptyInstance()
  {
    @SuppressWarnings("unchecked")
    Viewset<V> empty = (Viewset<V>) EMPTY;
    return empty;
  }

  @Override
  public T get(Object aKey)
  {
    return null;
  }

  @Override
  public Iterator<T> iterator()
  {
    return Collections.emptyIterator();
  }

  @Override
  public Viewset<?> keySet()
  {
    return this;
  }
}
