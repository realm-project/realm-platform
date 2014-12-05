package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;

import org.apache.commons.logging.Log;

public abstract class IResponder<T> extends IHandler<T> implements
    Service<T, Object>
{
  public static final String RESPONSE_CHAIN = "response";
  private final Handler<Object> theResponseHandler;

  public IResponder(Handler<Object> aResponseHandler)
  {
    this(DEFAULT_CONTEXT, aResponseHandler);
  }

  public IResponder(IHandlerContext aContext, Handler<Object> aResponseHandler)
  {
    this(aContext, aContext.getStop(), aResponseHandler, null);
  }

  public IResponder(IHandlerContext aContext, Handler<Object> aDefaultHandler,
      Handler<Object> aResponseHandler, Log aLog)
  {
    super(aContext, aDefaultHandler, aLog);
    theResponseHandler = aResponseHandler;
  }

  @Override
  protected Handler<Object> getChain(String aName)
  {
    return aName.equals(RESPONSE_CHAIN) ? theResponseHandler : super
        .getChain(aName);
  }

  protected Handler<Object> getResponseChain()
  {
    return theResponseHandler;
  }

  protected void respond(Action aAction, Object aObject)
  {
    getResponseChain().execute(aAction, aObject);
  }
}
