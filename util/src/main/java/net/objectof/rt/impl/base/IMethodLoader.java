package net.objectof.rt.impl.base;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.objectof.aggr.Mapping;
import net.objectof.rt.Member;

public class IMethodLoader extends ILoader
{
  public static final ILoader INSTANCE = new IMethodLoader();
  private static final ILoader ANNOTATED_LOADER = new IAnnotatedLoader()
  {
    @Override
    protected IMethod defineMember(ILoadableInterface aType, String aSelector,
        Method aMethod)
    {
      if (Modifier.isStatic(aMethod.getModifiers()))
      {
        Class<?>[] params = aMethod.getParameterTypes();
        if (params.length > 0)
        {
          // TODO need to have the proper peer() for adapted classes.
          // Class<?> receiverType = aMethod.getParameterTypes()[0];
          // if (receiverType.isAssignableFrom(aType.peer()))
          // {
          return new IStaticMethod(aSelector, aMethod);
          // }
          // }
          // throw new RuntimeException("Illegal static method for '" +
          // aSelector
          // + "'.");
        }
      }
      return new IVirtualMethod(aSelector, aMethod);
    }
  };
  private static final ILoader MANIFEST_LOADER = new IManifestLoader()
  {
    @Override
    protected IMethod defineMember(ILoadableInterface aType, String aSelector,
        Method aMethod)
    {
      return new IVirtualMethod(aSelector, aMethod);
    }
  };

  @Override
  public void load(ILoadableInterface aType, Mapping<String, Member> aMap)
  {
    ILoader loader = getLoader(aType);
    loader.load(aType, aMap);
  }

  private ILoader getLoader(ILoadableInterface aType)
  {
    ILoader loader = aType.isAnnotated() ? ANNOTATED_LOADER : MANIFEST_LOADER;
    return loader;
  }
}
