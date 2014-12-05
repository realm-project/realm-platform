package net.objectof.corc.web.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpRequest extends StreamRequest
{
  public HttpServletRequest getHttpRequest();

  public HttpServletResponse getHttpResponse();
}
