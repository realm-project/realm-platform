package net.objectof.aggr.impl;

import net.objectof.aggr.Set;
import net.objectof.rt.Interface;

public class ISet<T> extends ICollection<T, T> implements Set<T>
{
  public ISet(Class<T> aClass, java.util.Set<T> aSet)
  {
    super(aClass, aClass, aSet);
  }

  public ISet(Interface aType, java.util.Set<T> aSet)
  {
    super(aType, aType, aSet);
  }

  @Override
  public T get(Object aKey)
  {
    @SuppressWarnings("unchecked")
    T cast = (T) aKey;
    return contains(cast) ? cast : null;
  }

  @Override
  public Set<T> keySet()
  {
    return this;
  }
}
