package net.objectof.rt.impl.base;

import net.objectof.rt.Instance;

public class IAppliedFunction extends IFunction
{
  private final Object[] theMessage;

  public IAppliedFunction(Object aReceiver, IMethod aMethod, Object[] aMessage)
  {
    super(aReceiver, aMethod);
    theMessage = aMessage;
  }

  @Override
  public Instance apply(Object... aMessage)
  {
    if (aMessage.length != 0)
    {
      throw new IllegalArgumentException(
          "Message length must equal the function's arity");
    }
    return this;
  }

  @Override
  public Object evaluate(Object... aMessage)
  {
    if (aMessage.length != 0)
    {
      throw new IllegalArgumentException(
          "Message length must equal the function's arity");
    }
    return super.evaluate(theMessage);
  }
}
