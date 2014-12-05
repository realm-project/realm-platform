package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Interface;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class IHandler<T> implements Handler<T>, Interface<T>
{
  protected static final IHandlerContext DEFAULT_CONTEXT = new IHandlerContext();
  private final IHandlerContext theContext;
  private final Handler<?> theDefaultHandler;
  private final Log theLog;

  public IHandler()
  {
    this(null);
  }

  public IHandler(Handler<?> aDefault)
  {
    this(DEFAULT_CONTEXT, aDefault, null);
  }

  public IHandler(IHandlerContext aContext, Handler<?> aDefault, Log aLog)
  {
    theContext = aContext;
    if (aDefault == null && getContext() != null)
    {
      aDefault = getContext().getInvalidChannelHandler();
    }
    theDefaultHandler = aDefault;
    if (aLog == null)
    {
      aLog = LogFactory.getLog(getClass());
    }
    theLog = aLog;
  }

  @Override
  public void execute(Action aRequest, T aObject)
  {
    try
    {
      long start = processAction(aRequest, aObject);
      processEnd(aRequest, aObject, start);
    }
    catch (Exception e)
    {
      processFatalException(aRequest, aObject, e);
    }
  }

  @Override
  public Class<? extends Action> getActionClass()
  {
    return Action.class;
  }

  @Override
  public abstract Class<? extends T> getArgumentClass();

  @Override
  public Class<?> getReturnClass()
  {
    return Void.class;
  }

  @Override
  public Interface<T> getType()
  {
    return this;
  }

  public boolean isValidMessage(Action aRequest, T aObject)
  {
    return getActionClass().isInstance(aRequest)
        && (aObject == null || getArgumentClass().isInstance(aObject));
  }

  /**
   * Override to determine if timings are to be recorded in production.
   */
  public boolean record()
  {
    return true;
  }

  protected void chain(Action aRequest, Object aObject)
  {
    getChain("").execute(aRequest, aObject);
  }

  protected Handler<Object> getChain()
  {
    @SuppressWarnings("unchecked")
    Handler<Object> def = (Handler<Object>) theDefaultHandler;
    return def;
  }

  protected Handler<Object> getChain(String aName)
  {
    return aName.equals("") ? getChain() : getContext()
        .getInvalidChannelHandler();
  }

  protected final IHandlerContext getContext()
  {
    return theContext;
  }

  protected final Log log()
  {
    return theLog;
  }

  /**
   * Logs fatal exceptions. If we can't log them, print them to standard error.
   * If we can't do that, the exception will
   */
  protected void logFatal(Object aMessage, Exception aException)
  {
    try
    {
      log().fatal(aMessage, aException);
    }
    catch (Exception e)
    {
      System.err.println("Unable to log exceptions: " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  protected Action newAction(Action aAction, String aName)
  {
    return new IAction(aAction, aName);
  }

  /**
   * This implementation simply logs the exception.
   */
  protected void onException(Action aRequest, T aObject, Exception aException)
  {
    log().error(aRequest, aException);
  }

  protected void onExecute(Action aRequest, T aObject) throws Exception
  {
    chain(aRequest, aObject);
  }

  protected void onInvalidMessage(Action aRequest, T aObject)
  {
    String reason = getArgumentClass().isInstance(aRequest) //
    ? "Invalid argument object class."
        : "Invalid Action class";
    log().error("Invalid Request '" + aRequest + "': " + reason);
  }

  protected long processAction(Action aRequest, T aObject)
  {
    long start = processStart(aRequest, aObject);
    processExecute(aRequest, aObject, start);
    return start;
  }

  protected void processEnd(Action aRequest, T aObject, long aStart)
  {
    if (log().isDebugEnabled())
    {
      log().debug("END " + aRequest);
    }
    recordTime(aRequest, aStart);
  }

  protected void processExecute(Action aRequest, T aObject, long aStart)
  {
    try
    {
      onExecute(aRequest, aObject);
    }
    catch (Exception e)
    {
      onException(aRequest, aObject, e);
    }
  }

  /**
   * The default behavior is throw a runtime exception, i.e. this method doesn't
   * return normally. Override to change this behavior.
   */
  protected void processFatalException(Action aRequest, T aObject, Exception aEx)
  {
    logFatal(aRequest, aEx);
    if (aEx instanceof RuntimeException == false)
    {
      aEx = new RuntimeException(aEx);
    }
    throw (RuntimeException) aEx;
  }

  protected long processStart(Action aRequest, T aObject)
  {
    long start = System.currentTimeMillis();
    if (log().isDebugEnabled())
    {
      log().debug("START " + aRequest);
    }
    if (!isValidMessage(aRequest, aObject))
    {
      onInvalidMessage(aRequest, aObject);
    }
    return start;
  }

  protected void recordTime(Action aRequest, long aStart)
  {
    if (record())
    {
      getContext().getRecorder(this).record(this, aRequest, aStart);
    }
  }
}
