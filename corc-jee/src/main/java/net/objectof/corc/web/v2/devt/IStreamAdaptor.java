package net.objectof.corc.web.v2.devt;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.StreamRequest;
import net.objectof.impl.corc.requests.IRequestor;
import net.objectof.impl.corc.util.IRegister;

public class IStreamAdaptor extends IRequestor<StreamRequest>
{
  private final Service<StreamRequest, Object> theUnmarshaller;

  public IStreamAdaptor(IRegister aRegister, Handler<Object> aForwarder,
      Service<StreamRequest, Object> aUnmarshaller)
  {
    super(aRegister, aForwarder);
    theUnmarshaller = aUnmarshaller;
  }

  @Override
  public Class<? extends HttpRequest> getArgumentClass()
  {
    return HttpRequest.class;
  }

  @Override
  protected void onExecute(Action aAction, StreamRequest aRequest)
      throws Exception
  {
    Object object = theUnmarshaller.service(aAction, aRequest);
    chain(aAction, object);
  }
}
