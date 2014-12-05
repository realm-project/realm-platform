package net.objectof.model.corc;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;

public class IWebPing extends IHandler<HttpRequest>
{
  @Override
  public Class<? extends HttpRequest> getArgumentClass()
  {
    return HttpRequest.class;
  }

  @Override
  protected void onExecute(Action aRequest, HttpRequest aObject)
      throws Exception
  {
    aObject.getWriter().append("Hello, world!\n");
  }
}
