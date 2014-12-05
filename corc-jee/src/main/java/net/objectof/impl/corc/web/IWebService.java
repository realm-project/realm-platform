package net.objectof.impl.corc.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.web.WebService;

public abstract class IWebService extends HttpServlet implements
    WebService
{
  private static final long serialVersionUID = 1L;
  private WebService theService;

  @Override
  public void init(ServletConfig aConfig) throws ServletException
  {
    theService = initService(aConfig);
  }

  @Override
  public void service(HttpServletRequest aRequest, HttpServletResponse aResponse)
      throws ServletException, IOException
  {
    theService.service(aRequest, aResponse);
  }

  protected abstract WebService initService(ServletConfig aConfig);
}
