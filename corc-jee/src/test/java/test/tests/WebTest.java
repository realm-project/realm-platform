package test.tests;

import java.io.StringWriter;
import java.io.Writer;

import net.objectof.web.WebService;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import test.components.WebResponse;

public class WebTest
{
  public ApplicationContext ctx = new ClassPathXmlApplicationContext(
      "webContext.xml");

  @Test
  public void test() throws Exception
  {
    MockServletContext servletctx = new MockServletContext();
    MockHttpServletRequest request = new MockHttpServletRequest(
        servletctx,
        "GET",
        "http://localhost:9080/net.objectof.corc.web.test/test/echo.txt/test.objectof.org:20140101/ctx/Person");
    request.setPathInfo("/echo.txt/test.objectof.org:20140101/ctx/Person");
    Writer w = new StringWriter();
    WebResponse response = new WebResponse(w);
    WebService service = ctx.getBean(WebService.class);
    service.service(request, response);
    System.out.println(w.toString());
  }
}
