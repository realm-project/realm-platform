package net.objectof.rt.impl.base;

import java.util.Map;

import net.objectof.Context;
import net.objectof.aggr.Aggregate;
import net.objectof.aggr.Set;
import net.objectof.aggr.impl.ISet;
import net.objectof.rt.impl.IContext;

public class IUtilContext<T> extends IContext<T> implements Aggregate<String, T>
{
  public IUtilContext(Context<T> aParentContext, String aName)
  {
    super(aParentContext, aName);
  }

  public IUtilContext(Context<T> aParentContext, String aName,
      Map<String, T> aMap)
  {
    super(aParentContext, aName, aMap);
  }

  public IUtilContext(String aName)
  {
    super(aName);
  }

  public IUtilContext(String aName, Map<String, T> aMap)
  {
    super(aName, aMap);
  }

  @Override
  public T get(Object aKey)
  {
    return forName(aKey.toString());
  }

  @Override
  public Set<String> keySet()
  {
    return new ISet<String>(runtime().forClass(String.class), getMap().keySet());
  }

  @Override
  public T set(String aName, T aValue)
  {
    return getMap().put(aName, aValue);
  }

  @Override
  public int size()
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }
}
