package test.components;

import net.objectof.corc.Action;
import net.objectof.corc.Service;
import net.objectof.impl.corc.requests.ICollectingRequest;
import net.objectof.impl.corc.requests.IRequestor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServicePoint
{
  private final ApplicationContext theContext;
  private final String theContextName;
  private long theLastNumber = -System.currentTimeMillis();

  public ServicePoint(String aContextName)
  {
    theContextName = aContextName;
    theContext = createContext();
  }

  public Object perform(String aOperation, Object aObject)
  {
    @SuppressWarnings("unchecked")
    Service<Object, Object> service = theContext.getBean(aOperation,
        Service.class);
    Action request = new ICollectingRequest((IRequestor<?>) service,
        createRequestId(), aOperation, "john@objectof.org");
    System.out.println(request);
    service.service(request, aObject);
    System.out.println(request);
    return request;
  }

  protected ApplicationContext createContext()
  {
    return new ClassPathXmlApplicationContext(theContextName + "Context.xml");
  }

  protected String createRequestId()
  {
    return theContextName + "-" + --theLastNumber;
  }
}
