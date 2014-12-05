package net.objectof.impl.corc.web;

import javax.servlet.http.HttpServletRequest;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;
import net.objectof.impl.corc.util.IRegister;

public class IWebAdaptor extends IWebHandler
{
  private final Service<HttpServletRequest, Object> theUnmarshaller;

  public IWebAdaptor(IRegister aRegister, Handler<Object> aForwarder,
      Service<HttpServletRequest, Object> aUnmarshaller)
  {
    super(aRegister, aForwarder);
    theUnmarshaller = aUnmarshaller;
  }

  @Override
  protected void onExecute(Action aRequest, HttpServletRequest aObject)
      throws Exception
  {
    Object object = theUnmarshaller.service(aRequest, aObject);
    chain(aRequest, object);
  }
}
