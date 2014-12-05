package net.objectof.rt;

import net.objectof.InvalidNameException;
import net.objectof.Receiver;
import net.objectof.Selector;

public interface Instance extends Receiver
{
  public Instance apply(Object... aMessage);

  public Object evaluate(Object... aMessage);

  @Selector("select:")
  public Instance select(String aSelector) throws InvalidNameException;

  // @Selector("select:with:")
  // public Instance select(String aSelector, Object... aMessage)
  // throws InvalidNameException;
  @Selector
  Interface type();
}
