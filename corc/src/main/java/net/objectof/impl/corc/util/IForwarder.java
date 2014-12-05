package net.objectof.impl.corc.util;

import net.objectof.corc.Handler;
import net.objectof.impl.corc.IUtilHandler;

public class IForwarder extends IUtilHandler
{
  public IForwarder(Handler<Object> aForwarder)
  {
    super(aForwarder);
  }
}
