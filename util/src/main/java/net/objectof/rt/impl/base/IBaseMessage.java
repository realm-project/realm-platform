package net.objectof.rt.impl.base;

import net.objectof.ext.Vector;
import net.objectof.rt.Argument;
import net.objectof.rt.Invocation;

public class IBaseMessage implements Invocation, Vector<Argument>
{
  private final String theSelector;
  private final Argument theReceiver;
  private final Argument[] theArguments;

  public IBaseMessage(String aSelector, Argument aReceiver, Argument[] aMessage)
  {
    theSelector = aSelector;
    theReceiver = aReceiver;
    theArguments = aMessage;
  }

  @Override
  public Argument get(int aIndex)
  {
    return theArguments[aIndex];
  }

  @Override
  public Vector<Argument> getArguments()
  {
    return this;
  }

  @Override
  public Argument getReceiver()
  {
    return theReceiver;
  }

  @Override
  public String getSelector()
  {
    return theSelector;
  }

  @Override
  public int size()
  {
    return theArguments.length;
  }
}
