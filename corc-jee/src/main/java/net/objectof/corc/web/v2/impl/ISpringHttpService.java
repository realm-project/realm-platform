package net.objectof.corc.web.v2.impl;

import javax.servlet.ServletConfig;

import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ISpringHttpService extends IHttpService
{
  private static final long serialVersionUID = 1L;

  public ISpringHttpService()
  {
  }

  @Override
  protected Handler<HttpRequest> createHandler(ServletConfig aConfig)
  {
    String handlerName = aConfig.getInitParameter("handler");
    ApplicationContext ctx = WebApplicationContextUtils
        .getRequiredWebApplicationContext(aConfig.getServletContext());
    @SuppressWarnings("unchecked")
    Handler<HttpRequest> handler = (Handler<HttpRequest>) ctx
        .getBean(handlerName);
    return handler;
  }
}
