package net.objectof.impl.corc.requests;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.IHandler;
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
public abstract class IRequestor<T> extends IHandler<T>
{
  private final IRegister theRegister;

  public IRequestor(IHandlerContext aContext, IRegister aRegister,
      Handler<Object> aForwarder, Log aLog)
  {
    super(aContext, aForwarder, aLog);
    theRegister = aRegister;
  }

  public IRequestor(IRegister aRegister, Handler<Object> aForwarder)
  {
    this(DEFAULT_CONTEXT, aRegister, aForwarder, null);
  }

  @Override
  public Class<? extends IRequest> getActionClass()
  {
    return IRequest.class;
  }

  public int getTimeout()
  {
    return getContext().getDefaultTimeout();
  }

  public void receive(IRequest aRequest, Action aResponseAction, Object aObject)
      throws Exception
  {
    if (log().isDebugEnabled())
    {
      log().debug("RECEIVE " + aResponseAction);
    }
    aRequest.setResponse(aResponseAction, aObject);
    if (!isRequestActive(aRequest))
    {
      synchronized (aRequest)
      {
        aRequest.notify();
      }
    }
  }

  protected void block(Action aRequest)
  {
    try
    {
      synchronized (aRequest)
      {
        if (isRequestActive(aRequest))
        {
          if (log().isDebugEnabled())
          {
            log().debug(this + ": waiting for responses.");
          }
          aRequest.wait(getTimeout());
          if (log().isDebugEnabled())
          {
            log().debug(this + ": done waiting.");
          }
        }
      }
    }
    catch (InterruptedException e)
    {
      log().info(this + ": waiting interrupted.");
    }
  }

  protected void deregister(IRequest aRequest)
  {
    theRegister.deRegister(aRequest.getRequestId());
  }

  protected boolean isRequestActive(Action aRequest)
  {
    return false;
  }

  protected void onTimeout(Action aRequest, T aObject, long aStart)
  {
  }

  @Override
  protected void processExecute(Action aRequest, T aObject, long aStart)
  {
    // Safe cast assuming the Action class was validated prior.
    IRequest request = (IRequest) aRequest;
    register(request);
    super.processExecute(aRequest, aObject, aStart);
    block(request);
    deregister(request);
    if (isRequestActive(request))
    {
      processTimeout(aRequest, aObject, aStart);
    }
  }

  protected void processTimeout(Action aRequest, T aObject, long aStart)
  {
    if (log().isDebugEnabled())
    {
      log().debug(
          this + ": started at " + aStart + " with a timeout of "
              + getTimeout() + "ms is incomplete.  Calling onTimeout().");
    }
    onTimeout(aRequest, aObject, aStart);
  }

  protected void register(IRequest aRequest)
  {
    theRegister.register(aRequest.getRequestId(), aRequest);
  }
}
