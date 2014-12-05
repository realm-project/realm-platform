package net.objectof.corc.web.v2.devt;

import java.io.IOException;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.requests.IRequestor;
import net.objectof.impl.corc.util.IRegister;

public class IWebHandler extends IRequestor<HttpRequest> implements
    Handler<HttpRequest>
{
  public IWebHandler(IRegister aRegister, Handler<Object> aForwarder)
  {
    super(aRegister, aForwarder);
  }

  @Override
  public final Class<HttpRequest> getArgumentClass()
  {
    return HttpRequest.class;
  }

  protected boolean isMethodSupported(String aMethod)
  {
    return true;
  }

  @Override
  protected void onExecute(Action aAction, HttpRequest aRequest)
      throws Exception
  {
    if (!isMethodSupported(aRequest.getHttpRequest().getMethod()))
    {
      aRequest.getHttpResponse().sendError(405);
      return;
    }
    chain(aAction, aRequest);
  }

  @Override
  protected void onTimeout(Action aAction, HttpRequest aRequest, long aStart)
  {
    try
    {
      if (aRequest.getHttpResponse().isCommitted())
      {
        aRequest.getWriter().append("\n***TIMEOUT***\n");
        // resp.getWriter().close();
      }
      else
      {
        aRequest.getHttpResponse().sendError(504);
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
