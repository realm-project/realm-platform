package test.components;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.springframework.mock.web.MockHttpServletResponse;

public class WebResponse extends MockHttpServletResponse
{
  public PrintWriter w;

  public WebResponse(Writer aWriter)
  {
    w = new PrintWriter(aWriter);
  }

  /*
   * TODO The mock object returns an object but the api defines a String. It may
   * be a problems with versioning between the versions being referenced in the
   * maven dependencies.
   */
  @Override
  public String getHeader(String aName)
  {
    return super.getHeader(aName).toString();
  }

  @Override
  public PrintWriter getWriter() throws UnsupportedEncodingException
  {
    return w;
  }
}
