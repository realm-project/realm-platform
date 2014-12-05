package test.components;

import javax.servlet.http.HttpServletRequest;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.util.IRegister;
import net.objectof.impl.corc.web.IWebHandler;

public class TestWebServiceHandler extends IWebHandler
{
  private static int LAST_REQ = 0;

  public static synchronized String getNewRequestNumber()
  {
    return Integer.toHexString(++LAST_REQ);
  }

  public TestWebServiceHandler(IRegister aRegister, Handler<Object> aForwarder)
  {
    super(aRegister, aForwarder);
  }

  @Override
  protected boolean isMethodSupported(String aMethod)
  {
    return aMethod == "GET";
  }

  @Override
  protected void onExecute(Action aRequest, HttpServletRequest aObject)
      throws Exception
  {
    chain(newAction(aRequest, "fast"), aObject.getRequestURI());
    chain(newAction(aRequest, "slow"), "slow boat to china...");
  }
}
