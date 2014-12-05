package net.objectof.impl.corc.requests;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;
import net.objectof.impl.corc.IHandlerContext;
import net.objectof.impl.corc.util.IRegister;

import org.apache.commons.logging.Log;

/**
 * An abstract class to manage a transaction. Concrete classes:
 * <ul>
 * <li>override onExecute() to send() sub-requests on based on this transaction.
 * <li>override getStatus() to determine when the transaction is completed.
 * </ul>
 * 
 * @author jdh
 * 
 * @param <T>
 */
public abstract class IService<T, R> extends IRequestor<T> implements
    Service<T, R>
{
  private int theTimeout = 500;;

  public IService(IHandlerContext aContext, IRegister aRegister,
      Handler<Object> aForwarder, Log aLog)
  {
    super(aContext, aRegister, aForwarder, aLog);
  }

  public IService(IRegister aRegister, Handler<Object> aForwarder)
  {
    this(DEFAULT_CONTEXT, aRegister, aForwarder, null);
  }

  @Override
  public void execute(Action aRequest, T aObject)
  {
    service(aRequest, aObject);
  }

  @Override
  public abstract Class<? extends R> getReturnClass();

  @Override
  public int getTimeout()
  {
    return theTimeout;
  }

  @Override
  public R service(Action aRequest, T aObject)
  {
    try
    {
      long start = processAction(aRequest, aObject);
      R response = processResponse(aRequest, aObject);
      processEnd(aRequest, aObject, start);
      return response;
    }
    catch (Exception e)
    {
      processFatalException(aRequest, aObject, e);
      return null;
    }
  }

  public void setTimeout(int aTimeout)
  {
    theTimeout = aTimeout;
  }

  protected abstract R onResponse(Action aAction, T aObject) throws Exception;

  protected R processResponse(Action aRequest, T aObject) throws Exception
  {
    return onResponse(aRequest, aObject);
  }
}
