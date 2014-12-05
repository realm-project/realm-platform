package net.objectof.corc.dev;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

public class IResponse implements Response, Serializable
{
  public static final ResourceBundle BUNDLE = ResourceBundle
      .getBundle(Response.class.getName());
  private static final long serialVersionUID = 1L;
  private final Object theObject;

  public IResponse(Object aObject)
  {
    theObject = aObject;
  }

  public long count()
  {
    return size();
  }

  @Override
  public Category getCategory()
  {
    return Category.Success;
  }

  @Override
  public String getMessage()
  {
    return BUNDLE.getString(getMessageId());
  }

  @Override
  public String getMessageId()
  {
    return "SUCCESS";
  }

  @Override
  public Object getObject()
  {
    return theObject;
  }

  @Override
  public Iterator<Response> iterator()
  {
    return Collections.emptyIterator();
  }

  @Override
  public int size()
  {
    return 0;
  }

  @Override
  public String toString()
  {
    StringBuilder b = new StringBuilder(getCategory().toString());
    b.append(" [").append(getMessageId()).append("] ").append(getMessage());
    Object object = getObject();
    if (object != null)
    {
      b.append("; ").append(object);
    }
    return b.toString();
  }
}
