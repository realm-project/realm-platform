package net.objectof.impl.corc;

import net.objectof.corc.Handler;

import org.apache.commons.logging.Log;

public class IUtilHandler extends IHandler<Object>
{
  public IUtilHandler()
  {
    this(null);
  }

  public IUtilHandler(Handler<Object> aHandler)
  {
    this(DEFAULT_CONTEXT, aHandler, null);
  }

  public IUtilHandler(IHandlerContext aContext, Handler<Object> aHandler,
      Log aLog)
  {
    super(aContext, aHandler, aLog);
  }

  @Override
  public Class<? extends Object> getArgumentClass()
  {
    return Object.class;
  }

  @Override
  public boolean record()
  {
    return false;
  }
}
