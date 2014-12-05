package net.objectof.rt.impl.base;

import java.lang.reflect.Method;

import net.objectof.Selector;
import net.objectof.aggr.Mapping;
import net.objectof.rt.Member;

public abstract class IAnnotatedLoader extends IBaseLoader
{
  @Override
  protected void loadMembers(ILoadableInterface aType,
      Mapping<String, Member> aMap)
  {
    Method[] methods = aType.getMethods();
    int length = methods.length;
    for (int i = 0; i < length; i++)
    {
      Method method = methods[i];
      Selector annot = method.getAnnotation(Selector.class);
      if (annot != null)
      {
        String selector = annot.value();
        if (selector.equals(""))
        {
          selector = method.getName();
        }
        Member member = defineMember(aType, selector, method);
        aMap.put(selector, member);
      }
    }
  }
}
