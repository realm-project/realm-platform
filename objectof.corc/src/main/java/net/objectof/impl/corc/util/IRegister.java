package net.objectof.impl.corc.util;

import java.util.HashMap;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.IUtilHandler;

public class IRegister extends IUtilHandler
{
  private final HashMap<String, Handler<?>> theHandlers;

  public IRegister()
  {
    super();
    theHandlers = new HashMap<String, Handler<?>>();
  }

  public synchronized Handler<?> deRegister(String aId)
  {
    return theHandlers.remove(aId);
  }

  public Handler<?> register(String aId, Handler<?> aHandler)
  {
    return theHandlers.put(aId, aHandler);
  }

  @Override
  protected void onExecute(Action aAction, Object aObject)
  {
    @SuppressWarnings("unchecked")
    Handler<Object> handler = (Handler<Object>) theHandlers.get(aAction
        .getRequestId());
    if (handler == null)
      throw new NullPointerException(aAction.getRequestId());
    handler.execute(aAction, aObject);
  }
}
