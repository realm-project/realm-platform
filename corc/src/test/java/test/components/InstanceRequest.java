package test.components;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.impl.corc.requests.ICollectingRequest;
import net.objectof.impl.corc.requests.IService;
import net.objectof.impl.corc.util.IRegister;

public class InstanceRequest extends IService<String, String>
{
  public InstanceRequest(IRegister aRequests, Handler<Object> aForwarder)
  {
    super(aRequests, aForwarder);
  }

  @Override
  public Class<? extends String> getArgumentClass()
  {
    return String.class;
  }

  @Override
  public Class<? extends String> getReturnClass()
  {
    return String.class;
  }

  @Override
  public boolean isRequestActive(Action aRequest)
  {
    return ((ICollectingRequest) aRequest).getEntries().size() == 2 ? false
        : true;
  }

  @Override
  protected void onExecute(Action aAction, String aObject)
  {
    chain(newAction(aAction, "fast"), aObject);
    chain(newAction(aAction, "slow"), aObject);
  }

  @Override
  protected String onResponse(Action aRequest, String aObject)
  {
    return aRequest.toString();
  }
}
