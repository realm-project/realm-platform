package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.ex.ConfigurationException;

public class IHandlerContext
{
  private final Handler<Object> theInvalidChannelHandler = new IUtilHandler(
      null)
  {
    @Override
    protected void onExecute(Action aRequest, Object aObject)
    {
      throw new ConfigurationException(aRequest + ": InvalidChannel.execute()");
    }
  };
  private final Handler<Object> theStop = new IUtilHandler(null)
  {
    @Override
    protected void onExecute(Action aRequest, Object aObject)
    {
    }
  };

  public String getDefaultExceptionMessageId()
  {
    return "EXCEPTION";
  }

  public int getDefaultTimeout()
  {
    return 1000;
  }

  public Handler<Object> getInvalidChannelHandler()
  {
    return theInvalidChannelHandler;
  }

  public IRecorder getRecorder(Handler<?> aHandler)
  {
    return ILogRecorder.INSTANCE;
  }

  public Handler<Object> getStop()
  {
    return theStop;
  }

  public String getSuccessMessageId()
  {
    return "SUCCESS";
  }
}
