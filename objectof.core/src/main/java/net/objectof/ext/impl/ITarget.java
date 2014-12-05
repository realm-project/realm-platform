package net.objectof.ext.impl;

import java.io.IOException;

import net.objectof.Receiver;
import net.objectof.Selector;
import net.objectof.ext.ContentType;
import net.objectof.ext.Target;
import net.objectof.rt.impl.IFn;

@Selector
public class ITarget extends IFn implements Target
{
  private final Appendable theOut;
  private final ContentType theContentType;

  public ITarget(ContentType aContentType, Appendable aOut)
  {
    theContentType = aContentType;
    theOut = aOut;
  }

  @Override
  public void append(Object aValue)
  {
    try
    {
      theOut.append(aValue.toString());
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Selector("callback:")
  public void callback(Receiver aFunction)
  {
    aFunction.perform(theContentType.getCallbackSelector(), theOut);
  }

  @Override
  public String getMediaType()
  {
    return theContentType.getMediaType();
  }

  @Override
  public Object getProperty(String aUniqueName)
  {
    return null;
  }

  @Override
  public Receiver getTranscoder()
  {
    return this;
  }

  @Override
  public String toString()
  {
    return theOut.toString();
  }
}
