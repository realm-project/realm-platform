package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;

public abstract class IRecorder
{
  public abstract void record(Handler<?> aHandler, Action aRequest, long aStart);
}
