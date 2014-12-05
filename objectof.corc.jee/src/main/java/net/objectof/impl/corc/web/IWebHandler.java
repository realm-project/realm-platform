package net.objectof.impl.corc.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.requests.IRequestor;
import net.objectof.impl.corc.util.IRegister;

public abstract class IWebHandler extends IRequestor<HttpServletRequest>
    implements Handler<HttpServletRequest>
{
  public IWebHandler(IRegister aRegister, Handler<Object> aForwarder)
  {
    super(aRegister, aForwarder);
  }

  @Override
  public Class<? extends IWebRequest> getActionClass()
  {
    return IWebRequest.class;
  }

  @Override
  public final Class<HttpServletRequest> getArgumentClass()
  {
    return HttpServletRequest.class;
  }

  protected boolean isMethodSupported(String aMethod)
  {
    return aMethod == "GET";
  }

  protected Object onInvalidMethod(HttpServletRequest aRequest,
      HttpServletResponse aResponse) throws IOException
  {
    // TODO need to set the allowed methods on a response header.
    aResponse.sendError(405);
    return "Invalid Method";
  }

  @Override
  protected void onTimeout(Action aRequest, HttpServletRequest aObject,
      long aStart)
  {
    IWebRequest req = (IWebRequest) aRequest;
    HttpServletResponse resp = req.getHttpResponse();
    try
    {
      if (resp.isCommitted())
      {
        resp.getWriter().print("\n***TIMEOUT***\n");
        resp.getWriter().close();
      }
      else
      {
        resp.sendError(504);
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  protected Object onValidateRequest(IWebRequest aRequest) throws IOException
  {
    if (!isMethodSupported(aRequest.getHttpRequest().getMethod()))
      return onInvalidMethod(aRequest.getHttpRequest(),
          aRequest.getHttpResponse());
    return null;
  }
}
