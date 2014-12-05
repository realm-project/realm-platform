package net.objectof.rt;

import net.objectof.ext.Vector;

public interface Invocation
{
  public Vector<? extends Argument> getArguments();

  public Argument getReceiver();

  public String getSelector();
}
