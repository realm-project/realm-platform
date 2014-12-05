package net.objectof.impl.corc.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Handler;
import net.objectof.corc.ex.ConfigurationException;
import net.objectof.impl.corc.requests.IRequest;
import net.objectof.impl.corc.util.IRouter;
import net.objectof.web.WebService;

public abstract class IWebServiceRouter extends IRouter implements WebService
{
  public static final String nameFrom(HttpServletRequest aRequest)
  {
    String path = aRequest.getPathInfo();
    if (path == null || path.equals("/"))
    {
      path = "/";
    }
    else
    {
      int idx = path.indexOf('/', 1);
      if (idx < 0)
      {
        idx = path.length();
      }
      path = path.substring(1, idx);
    }
    return path;
  }

  public IWebServiceRouter(Map<String, Handler<Object>> aMap)
  {
    super(aMap);
  }

  @Override
  public final void service(HttpServletRequest aRequest,
      HttpServletResponse aResponse) throws ServletException, IOException
  {
    Object actor = authenticate(aRequest);
    if (actor == null)
    {
      onAuthenticationFailure(aRequest, aResponse);
      return;
    }
    String name = nameFor(aRequest);
    Handler<?> handler = getChain(name);
    if (handler == getContext().getInvalidChannelHandler())
    {
      onHandlerNotFound(aRequest, aResponse);
      return;
    }
    // Safe cast to IWebHandler assuming verifyHandler check executed.
    IRequest action = createRequest(aRequest, aResponse, name, actor,
        (IWebHandler) handler);
    if (action != null)
    {
      execute(action, aRequest);
    }
  }

  /**
   * Authorizes the request.
   * 
   * @param aRequest
   *          The request to authorize.
   * @return The actor object or null when the request could not be authorized.
   */
  protected abstract Object authenticate(HttpServletRequest aRequest);

  protected abstract IRequest createRequest(HttpServletRequest aRequest,
      HttpServletResponse aResponse, String aName, Object aActor,
      IWebHandler aHandler) throws IOException;

  protected String nameFor(HttpServletRequest aRequest)
  {
    return nameFrom(aRequest);
  }

  protected void onAuthenticationFailure(HttpServletRequest aRequest,
      HttpServletResponse aResponse) throws IOException
  {
    aResponse.sendError(403);
  }

  protected void onHandlerNotFound(HttpServletRequest aRequest,
      HttpServletResponse aResponse) throws IOException
  {
    aResponse.sendError(404);
  }

  @Override
  protected void verifyHandler(String aName, Handler<?> aHandler)
      throws ConfigurationException
  {
    if (aHandler instanceof IWebHandler == false)
      throw new ConfigurationException("'" + aName + "' must be an IWebHandler");
  }
}
