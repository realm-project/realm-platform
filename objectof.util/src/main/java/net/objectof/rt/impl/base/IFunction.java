package net.objectof.rt.impl.base;

import net.objectof.EvaluationException;
import net.objectof.ext.Vector;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.impl.IFn;

public class IFunction extends IFn implements Instance
{
  private final Object theReceiver;
  private final IMethod theMethod;

  public IFunction(Object aReceiver, IMethod aMethod)
  {
    theReceiver = aReceiver;
    theMethod = aMethod;
  }

  @Override
  public Instance apply(Object... aMessage)
  {
    Vector<? extends Interface> parameters = theMethod.getParameters();
    int arity = parameters.size();
    if (aMessage.length != parameters.size())
    {
      throw new IllegalArgumentException(
          "Message length must equal the function's arity");
    }
    if (arity == 0)
    {
      return this;
    }
    for (int i = 0; i < arity; i++)
    {
      Interface parameter = parameters.get(i);
      Object arg = aMessage[i];
      if (arg != null)
      {
        if (!parameter.isInstance(arg))
        {
          throw new IllegalArgumentException("Argument '" + i + "' must be a '"
              + parameter.getUniqueName() + "'");
        }
      }
    }
    return new IAppliedFunction(theReceiver, theMethod, aMessage);
  }

  @Override
  public Object evaluate(Object... aMessage)
  {
    try
    {
      return theMethod.invoke(theReceiver, aMessage);
    }
    catch (Exception e)
    {
      throw new EvaluationException(e);
    }
  }
}
