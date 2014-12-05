package net.objectof.corc.web.v2.impl.testing;

import java.io.UnsupportedEncodingException;

import org.springframework.mock.web.MockHttpServletResponse;

public class ITestResponse extends MockHttpServletResponse
{
  @Override
  public String toString()
  {
    try
    {
      getWriter().flush();
      return getContentAsString();
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException();
    }
  }
}
