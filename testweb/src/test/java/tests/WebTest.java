package tests;


import javax.servlet.http.HttpServletResponse;

import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.impl.testing.ITestWebService;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class WebTest {

    public ApplicationContext ctx = new ClassPathXmlApplicationContext("webContext.xml");

    @Test
    public void test() throws Exception {
        @SuppressWarnings("unchecked")
        Handler<HttpRequest> handler = (Handler<HttpRequest>) ctx.getBean("router");
        ITestWebService service = new ITestWebService(handler);
        HttpServletResponse resp = service.get("/person/Person-1.json");
        System.out.println(resp.toString());
    }

    @Test
    public void test2() throws Exception {
        @SuppressWarnings("unchecked")
        Handler<HttpRequest> handler = (Handler<HttpRequest>) ctx.getBean("router");
        ITestWebService service = new ITestWebService(handler);
        HttpServletResponse resp = service.get("/ping");
        System.out.println(resp.toString());
    }
}
