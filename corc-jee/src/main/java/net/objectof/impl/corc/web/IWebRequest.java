package net.objectof.impl.corc.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.impl.corc.requests.IRequestor;
import net.objectof.impl.corc.requests.IStreamingRequest;

public class IWebRequest extends IStreamingRequest<Object>
{
  private final String theRequestId;
  private final HttpServletRequest theHttpRequest;
  private final HttpServletResponse theHttpResponse;

  public IWebRequest(IRequestor<?> aCollector, String aId,
      HttpServletRequest aRequest, HttpServletResponse aResponse)
  {
    super(aCollector);
    theRequestId = aId;
    theHttpRequest = aRequest;
    theHttpResponse = aResponse;
  }

  @Override
  public Class<?> getArgumentClass()
  {
    return Object.class;
  }

  @Override
  public Object getActor()
  {
    return "anonymous";
  }

  @Override
  public Appendable getAppender()
  {
    try
    {
      return theHttpResponse.getWriter();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public HttpServletRequest getHttpRequest()
  {
    return theHttpRequest;
  }

  public HttpServletResponse getHttpResponse()
  {
    return theHttpResponse;
  }

  @Override
  public String getName()
  {
    return IWebServiceRouter.nameFrom(theHttpRequest);
  }

  @Override
  public String getRequestId()
  {
    return theRequestId;
  }
}
