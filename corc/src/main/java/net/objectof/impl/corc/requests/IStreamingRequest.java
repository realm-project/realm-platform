package net.objectof.impl.corc.requests;

import java.io.IOException;

import net.objectof.corc.Action;

public abstract class IStreamingRequest<T> extends IRequest
{
  public IStreamingRequest(IRequestor<?> aCollector)
  {
    super(aCollector);
  }

  public Appendable append(Object aObject) throws IOException
  {
    return getAppender().append(aObject.toString());
  }

  public abstract Appendable getAppender();

  @Override
  public Void setResponse(Action aResponse, Object aObject)
  {
    try
    {
      append(aObject);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    return null;
  }
}
