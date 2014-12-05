package net.objectof.impl.corc.util;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.IUtilHandler;

public class IPing extends IUtilHandler
{
  private long theDelay = 1L;

  public IPing(Handler<Object> aForwarder)
  {
    super(aForwarder);
  }

  @Override
  public Class<? extends String> getArgumentClass()
  {
    return String.class;
  }

  public long getDelay()
  {
    return theDelay;
  }

  @Override
  public boolean record()
  {
    return true;
  }

  public void setDelay(long aDelay)
  {
    theDelay = aDelay;
  }

  @Override
  protected void onExecute(Action aRequest, Object aObject)
      throws InterruptedException
  {
    long delay = getDelay();
    synchronized (this)
    {
      log().debug("inserting a " + delay + "ms delay for testing.");
      this.wait(delay);
    }
    chain(aRequest, aRequest.getName() + ": " + aObject.toString());
  }
}
