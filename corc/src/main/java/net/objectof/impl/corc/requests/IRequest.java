package net.objectof.impl.corc.requests;

import net.objectof.corc.Action;
import net.objectof.impl.corc.IAction;
import net.objectof.impl.corc.IHandler;
import net.objectof.impl.corc.IHandlerContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class IRequest extends IHandler<Object> implements Action
{
  private static final Log LOG = LogFactory.getLog(IRequest.class);

  public IRequest(IHandlerContext aContext, IRequestor<?> aCollector)
  {
    super(aContext, aCollector, LOG);
  }

  public IRequest(IRequestor<?> aCollector)
  {
    super(DEFAULT_CONTEXT, aCollector, LOG);
  }

  @Override
  public abstract Object getActor();

  @Override
  public Class<? extends Object> getArgumentClass()
  {
    return Object.class;
  }

  @Override
  public abstract String getName();

  @Override
  public abstract String getRequestId();

  public Action newAction(String aRequestName)
  {
    return new IAction(this, aRequestName);
  }

  public abstract Object setResponse(Action aRequest, Object aObject);

  @Override
  public String toString()
  {
    return getName() + '@' + getActor() + '?' + getRequestId();
  }

  @Override
  protected IRequestor<Object> getChain()
  {
    return (IRequestor<Object>) super.getChain();
  }

  @Override
  protected final void onExecute(Action aRequest, Object aObject)
      throws Exception
  {
    getChain().receive(this, aRequest, aObject);
  }
}
