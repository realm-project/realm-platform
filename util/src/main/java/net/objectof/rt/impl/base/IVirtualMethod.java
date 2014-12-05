package net.objectof.rt.impl.base;

import java.lang.reflect.Method;

import net.objectof.EvaluationException;
import net.objectof.ext.Vector;
import net.objectof.rt.Interface;

public class IVirtualMethod extends IMethod implements Vector<Interface>
{
  public IVirtualMethod(String aSelector, Method aMethod)
  {
    super(aSelector, aMethod);
  }

  @Override
  public Interface get(int aIndex)
  {
    return runtime().forClass(getMethod().getParameterTypes()[aIndex]);
  }

  @Override
  public Vector<? extends Interface> getParameters()
  {
    return this;
  }

  @Override
  public Object invoke(Object aReceiver, Object[] aMessage)
  {
    try
    {
      return getMethod().invoke(aReceiver, aMessage);
    }
    catch (Exception e)
    {
      throw new EvaluationException(e);
    }
  }

  @Override
  public int size()
  {
    return getMethod().getParameterTypes().length;
  }
}
