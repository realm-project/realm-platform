package net.objectof.impl.corc;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ILogRecorder extends IRecorder
{
  public static final ILogRecorder INSTANCE = new ILogRecorder();
  private final Log theLog = LogFactory.getLog("TIMING");

  @Override
  public void record(Handler<?> aChain, Action aRequest, long aStart)
  {
    Log log = theLog;
    if (log.isInfoEnabled())
    {
      log.info(message(aChain, aRequest, aStart, System.currentTimeMillis()));
    }
    else if (theLog.isDebugEnabled())
    {
      log.debug(message(aChain, aRequest, aStart, System.currentTimeMillis()));
    }
  }

  protected String message(Handler<?> aHandler, Action aRequest, long aStart,
      long aEnd)
  {
    return aRequest.getRequestId() + ',' + (aEnd - aStart) + "ms," + aStart
        + ',' + aHandler.getClass().getName();
  }
}
