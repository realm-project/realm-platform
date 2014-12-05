package net.objectof.rt.impl.base;

import java.lang.reflect.Method;

import net.objectof.EvaluationException;
import net.objectof.ext.Vector;
import net.objectof.rt.Interface;

public class IStaticMethod extends IMethod implements Vector<Interface>
{
  public IStaticMethod(String aSelector, Method aMethod)
  {
    super(aSelector, aMethod);
  }

  @Override
  public Interface get(int aIndex)
  {
    return runtime().forClass(getMethod().getParameterTypes()[aIndex + 1]);
  }

  @Override
  public Vector<? extends Interface> getParameters()
  {
    return this;
  }

  @Override
  public Object invoke(Object aReceiver, Object[] aMessage)
  {
    Object[] args = new Object[aMessage.length + 1];
    args[0] = aReceiver;
    for (int i = 0; i < aMessage.length; i++)
    {
      args[i + 1] = aMessage[i];
    }
    try
    {
      return getMethod().invoke(null, args);
    }
    catch (Exception e)
    {
      throw new EvaluationException(e);
    }
  }

  @Override
  public Class<?> peer()
  {
    return getMethod().getParameterTypes()[0];
  }

  @Override
  public int size()
  {
    return getMethod().getParameterTypes().length - 1;
  }
}
