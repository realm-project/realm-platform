package net.objectof.impl.corc.requests;


public class IResponseRequest<R> extends ICollectingRequest
{
  private R theResponse;

  public IResponseRequest(IRequestor<?> aCollector, String aRequestId,
      String aName, Object aActor)
  {
    super(aCollector, aRequestId, aName, aActor);
  }

  public R getResponse()
  {
    return theResponse;
  }

  public R setResponse(R aResponse)
  {
    R prior = theResponse;
    theResponse = aResponse;
    return prior;
  }
}
