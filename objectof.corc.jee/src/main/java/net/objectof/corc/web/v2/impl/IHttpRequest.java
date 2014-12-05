package net.objectof.corc.web.v2.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.web.v2.HttpRequest;

public class IHttpRequest extends IStreamRequest implements HttpRequest
{
  private final HttpServletRequest theHttpRequest;
  private final HttpServletResponse theHttpResponse;

  public IHttpRequest(String aId, Object aActor, HttpServletRequest aRequest,
      HttpServletResponse aResponse)
  {
    super(aId, aActor);
    theHttpRequest = aRequest;
    theHttpResponse = aResponse;
  }

  @Override
  public HttpServletRequest getHttpRequest()
  {
    return theHttpRequest;
  }

  @Override
  public HttpServletResponse getHttpResponse()
  {
    return theHttpResponse;
  }

  /**
   * @return getHttpRequest().getRequestURI().substring(1)
   */
  @Override
  public String getPath()
  {
    return theHttpRequest.getRequestURI().substring(1);
  }

  @Override
  public Reader getReader() throws IOException
  {
    return theHttpRequest.getReader();
  }

  @Override
  public String getRequestType()
  {
    return theHttpRequest.getContentType();
  }

  @Override
  public String getResponseType()
  {
    return theHttpResponse.getContentType();
  }

  @Override
  public Writer getWriter() throws IOException
  {
    return theHttpResponse.getWriter();
  }
}
