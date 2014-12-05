package net.objectof.impl.corc.web.spring;

import javax.servlet.ServletConfig;

import net.objectof.impl.corc.web.IWebService;
import net.objectof.web.WebService;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class ISpringWebService extends IWebService
{
  private static final long serialVersionUID = 1L;

  public ISpringWebService()
  {
  }

  protected String getServiceName(ServletConfig aConfig)
  {
    return aConfig.getInitParameter("serviceName");
  }

  @Override
  protected WebService initService(ServletConfig aConfig)
  {
    return WebApplicationContextUtils.getRequiredWebApplicationContext(
        aConfig.getServletContext()).getBean(getServiceName(aConfig),
        WebService.class);
  }
}
