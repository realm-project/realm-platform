package net.objectof.corc.web.v2.impl.testing;

import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;

public class ITestRequest extends MockHttpServletRequest
{
  public ITestRequest(ServletContext aServletContext, String aMethod,
      String aRequestURI)
  {
    super(aServletContext, aMethod, aRequestURI);
  }

  public ITestRequest(String aMethod, String aRequestURI)
  {
    super(aMethod, aRequestURI);
  }
}
