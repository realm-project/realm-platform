package net.objectof.rt.impl.base;

import net.objectof.aggr.Mapping;
import net.objectof.rt.Member;
import net.objectof.rt.impl.IFn;

public abstract class ILoader extends IFn
{
  public static final ILoader NIL_INSTANCE = new ILoader()
  {
    @Override
    public void load(ILoadableInterface aType, Mapping<String, Member> aMap)
    {
    }
  };

  public abstract void load(ILoadableInterface aType,
      Mapping<String, Member> aMap);
}
