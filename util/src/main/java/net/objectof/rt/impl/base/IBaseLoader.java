package net.objectof.rt.impl.base;

import java.lang.reflect.Method;
import java.util.Map;

import net.objectof.aggr.Mapping;
import net.objectof.rt.Member;

public abstract class IBaseLoader extends ILoader
{
  @Override
  public void load(ILoadableInterface aType, Mapping<String, Member> aMap)
  {
    extend(aType, aMap);
    loadMembers(aType, aMap);
  }

  protected abstract Member defineMember(ILoadableInterface aType,
      String aSelector, Method aMethod);

  protected void extend(ILoadableInterface aType, Mapping<String, Member> aMap)
  {
    Class<?> superclass = aType.peer().getSuperclass();
    if (superclass != null)
    {
      extendFrom(aMap, superclass);
    }
    for (Class<?> superinterface : aType.peer().getInterfaces())
    {
      extendFrom(aMap, superinterface);
    }
  }

  protected void extendFrom(Map<String, Member> aMap, Class<?> aClass)
  {
    ILoadableInterface supertype = forClass(aClass);
    for (String selector : supertype.getMembers().keySet())
    {
      aMap.put(selector, supertype.forName(selector));
    }
  }

  protected ILoadableInterface forClass(Class<?> aClass)
  {
    ILoadableInterface forClass = (ILoadableInterface) runtime().forClass(
        aClass);
    return forClass;
  }

  protected abstract void loadMembers(ILoadableInterface aType,
      Mapping<String, Member> aMap);
}
