package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;

import org.apache.commons.logging.Log;

public abstract class IStep<T> extends IResponder<T> implements
    Service<T, Object>
{
  public IStep(Handler<Object> aResponseHandler)
  {
    super(aResponseHandler);
  }

  public IStep(IHandlerContext aContext, IHandler<Object> aDefaultHandler,
      Handler<Object> aResponseHandler, Log aLog)
  {
    super(aContext, aDefaultHandler, aResponseHandler, aLog);
  }

  @Override
  public abstract Object service(Action aRequest, T aObject);

  protected abstract Object nextArgument(Action aRequest, T aObject,
      Object aResponse);

  @Override
  protected final void onExecute(Action aRequest, T aObject) throws Exception
  {
    Object response = service(aRequest, aObject);
    respond(aRequest, response);
    response = nextArgument(aRequest, aObject, response);
    chain(aRequest, response);
  }
}
