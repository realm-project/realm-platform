package net.objectof.rt.impl.base;

import java.lang.reflect.Constructor;
import java.util.ResourceBundle;

import net.objectof.Context;
import net.objectof.ext.Domain;
import net.objectof.ext.Dominion;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.ILoadableContext;

@SuppressWarnings("unchecked")
public class IRuntimes extends ILoadableContext<Runtime<?>> implements Domain
{
  public static final String RUNTIMES = "rt.objectof.net:1401";
  public static final String DEFAULT = "java";
  public static final Context<Runtime<?>> INSTANCE = loadInstance();

  private static final Context<Runtime<?>> loadInstance()
  {
    Context<Runtime<?>> inst = (Context<Runtime<?>>) Dominion.INSTANCE
        .find(RUNTIMES);
    return inst;
  }

  public IRuntimes(String aName)
  {
    super(aName);
  }

  @Override
  public Object forPath(String aRuntime)
  {
    return forName(aRuntime);
  }

  @Override
  protected Runtime<?> load(String aRuntimeName)
  {
    ResourceBundle bundle = ResourceBundle.getBundle("rt." + aRuntimeName);
    String clsName = bundle.getString("class");
    try
    {
      Constructor<IRuntime<?>> constr = (Constructor<IRuntime<?>>) Class
          .forName(clsName).getConstructor(String.class);
      return constr.newInstance(aRuntimeName);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new Ex(aRuntimeName, e);
    }
  }
}
