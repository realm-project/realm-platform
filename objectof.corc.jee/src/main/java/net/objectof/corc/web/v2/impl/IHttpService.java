package net.objectof.corc.web.v2.impl;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;

public abstract class IHttpService extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  public static final long EPOCH = 1388552400000L;
  private String theServicePrefix;
  private Handler<HttpRequest> theHandler;
  private int theLastRequest;

  public IHttpService()
  {
    theLastRequest = (int) ((System.currentTimeMillis() - EPOCH) / 1000);
  }

  @Override
  public void init(ServletConfig aConfig) throws ServletException
  {
    setHandler(createHandler(aConfig));
    String id = aConfig.getInitParameter("servicePrefix");
    setServicePrefix(id == null ? "/" : id);
  }

  @Override
  public void service(HttpServletRequest aRequest, HttpServletResponse aResponse)
      throws ServletException, IOException
  {
    IHttpRequest req = createRequest(aRequest, aResponse);
    if (req != null)
    {
      theHandler.execute(req, req);
    }
  }

  /**
   * Authenticates a request. The default implementation does not authenticate
   * but simply returns "public" as credentials. Override to perform
   * authentication.
   * <p>
   * This method is responsible for determining the response (error code,
   * redirect, etc.) when the consumer cannot be authenticated.
   * 
   * @param aRequest
   * @param aResponse
   * @return An object containing credentials or null when authenticate fails.
   */
  protected Object authenticate(HttpServletRequest aRequest,
      HttpServletResponse aResponse)
  {
    return "public";
  }

  protected abstract Handler<HttpRequest> createHandler(ServletConfig aConfig);

  /**
   * 
   * @param aRequest
   * @param aResponse
   * @return An IWebRequest or null when the request should not be chained, such
   *         as when authentication is required.
   */
  protected IHttpRequest createRequest(HttpServletRequest aRequest,
      HttpServletResponse aResponse)
  {
    Object actor = authenticate(aRequest, aResponse);
    return actor == null ? null : new IHttpRequest(createRequestId(aRequest),
        actor, aRequest, aResponse);
  }

  protected synchronized String createRequestId(HttpServletRequest aRequest)
  {
    StringBuilder b = new StringBuilder(theServicePrefix);
    b.append(Integer.toHexString(++theLastRequest).toUpperCase());
    String clientId = aRequest.getHeader("correlationId");
    if (clientId != null)
    {
      b.append('/').append(clientId);
    }
    return b.toString();
  }

  protected Handler<HttpRequest> getHandler()
  {
    return theHandler;
  }

  protected void setHandler(Handler<HttpRequest> aHandler)
  {
    theHandler = aHandler;
  }

  protected void setServicePrefix(String aServiceId)
  {
    theServicePrefix = aServiceId;
  }
}
