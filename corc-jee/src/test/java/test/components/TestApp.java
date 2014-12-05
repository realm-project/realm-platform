package test.components;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Handler;
import net.objectof.impl.corc.requests.IRequest;
import net.objectof.impl.corc.web.IWebHandler;
import net.objectof.impl.corc.web.IWebRequest;
import net.objectof.impl.corc.web.IWebServiceRouter;

public class TestApp extends IWebServiceRouter
{
  private static int LAST_ID = 0;

  public TestApp(Map<String, Handler<Object>> aMap)
  {
    super(aMap);
  }

  @Override
  protected Object authenticate(HttpServletRequest aRequest)
  {
    return "anonymous";
  }

  @Override
  protected IRequest createRequest(HttpServletRequest aRequest,
      HttpServletResponse aResponse, String aName, Object aActor,
      IWebHandler aHandler)
  {
    return new IWebRequest(aHandler, createRequestId(aRequest), aRequest,
        aResponse);
  }

  protected synchronized String createRequestId(HttpServletRequest aRequest)
  {
    return Integer.toHexString(++LAST_ID);
  }

  protected void info(HttpServletRequest aRequest)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(" [");
    sb.append("\n\t request.getContextPath(): ").append(
        aRequest.getContextPath());
    sb.append("\n\t request.getLocalAddr(): ").append(aRequest.getLocalAddr());
    sb.append("\n\t request.getLocalName(): ").append(aRequest.getLocalName());
    sb.append("\n\t request.getLocalPort(): ").append(aRequest.getLocalPort());
    sb.append("\n\t request.getPathInfo(): ").append(aRequest.getPathInfo());
    sb.append("\n\t request.getPathTranslated(): ").append(
        aRequest.getPathTranslated());
    sb.append("\n\t request.getProtocol(): ").append(aRequest.getProtocol());
    sb.append("\n\t request.getRemoteAddr(): ")
        .append(aRequest.getRemoteAddr());
    sb.append("\n\t request.getRemoteHost(): ")
        .append(aRequest.getRemoteHost());
    sb.append("\n\t request.getRemotePort(): ")
        .append(aRequest.getRemotePort());
    sb.append("\n\t request.getRequestURI(): ")
        .append(aRequest.getRequestURI());
    sb.append("\n\t request.getRequestURL(): ")
        .append(aRequest.getRequestURL());
    sb.append("\n\t request.getScheme(): ").append(aRequest.getScheme());
    sb.append("\n\t request.getServerName(): ")
        .append(aRequest.getServerName());
    sb.append("\n\t request.getServerPort(): ")
        .append(aRequest.getServerPort());
    sb.append("\n\t request.getServletPath(): ").append(
        aRequest.getServletPath());
    sb.append("\n\t request.getQueryString(): ").append(
        aRequest.getQueryString());
    sb.append("\n]");
    System.out.println(sb);
  }

  @Override
  protected void onAuthenticationFailure(HttpServletRequest aRequest,
      HttpServletResponse aResponse) throws IOException
  {
    aResponse.setHeader("WWW-Authenticate", "Basic realm=\"testApp\"");
    aResponse.sendError(401);
  }
}
