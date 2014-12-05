package net.objectof.corc.web.v2.impl.testing;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.impl.IHttpService;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ITestWebService extends IHttpService
{
  public ITestWebService(Handler<HttpRequest> aHandler)
  {
    setHandler(aHandler);
    setServicePrefix("test/");
  }

  public HttpServletResponse get(String aURL) throws ServletException,
      IOException
  {
    HttpServletRequest request = new ITestRequest("GET", aURL);
    HttpServletResponse response = new ITestResponse();
    service(request, response);
    return response;
  }

  public HttpServletResponse post(String aURL, String aPayload)
      throws ServletException, IOException
  {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", aURL);
    request.setContent(aPayload.getBytes());
    MockHttpServletResponse response = new MockHttpServletResponse();
    service(request, response);
    return response;
  }

  @Override
  protected Handler<HttpRequest> createHandler(ServletConfig aConfig)
  {
    /* The handler is passed on the constructor for testing... just return it. */
    return getHandler();
  }
}
