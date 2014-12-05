package net.objectof.impl.corc.requests;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.IHandlerContext;
import net.objectof.impl.corc.util.IRegister;

import org.apache.commons.logging.Log;

public abstract class IResponseHandler<T, R> extends IService<T, R>
{
  public IResponseHandler(IHandlerContext aContext, IRegister aRegister,
      Handler<Object> aForwarder, Log aLog)
  {
    super(aContext, aRegister, aForwarder, aLog);
  }

  public IResponseHandler(IRegister aRegister, Handler<Object> aForwarder)
  {
    this(DEFAULT_CONTEXT, aRegister, aForwarder, null);
  }

  @Override
  protected R processResponse(Action aRequest, T aObject) throws Exception
  {
    @SuppressWarnings("unchecked")
    IResponseRequest<R> request = (IResponseRequest<R>) aRequest;
    R response = super.processResponse(aRequest, aObject);
    request.setResponse(response);
    return response;
  }
}
