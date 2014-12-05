package net.objectof.impl.corc.util;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;

public class IAsyncForwarder extends IForwarder
{
  public class ActionThread extends Thread
  {
    private final Handler<Object> theHandler;
    private final Action theAction;
    private final Object theObject;

    public ActionThread(Handler<Object> aHandler, Action aRequest,
        Object aObject)
    {
      theHandler = aHandler;
      theAction = aRequest;
      theObject = aObject;
    }

    @Override
    public void run()
    {
      theHandler.execute(theAction, theObject);
    }

    @Override
    public String toString()
    {
      return "Action " + theAction;
    }
  }

  public IAsyncForwarder(Handler<Object> aForwarder)
  {
    super(aForwarder);
  }

  @Override
  protected void onExecute(Action aRequest, Object aObject)
  {
    new ActionThread(getChain(""), aRequest, aObject).start();
  }
}
