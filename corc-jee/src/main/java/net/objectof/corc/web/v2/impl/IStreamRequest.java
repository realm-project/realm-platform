package net.objectof.corc.web.v2.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.StreamRequest;

public abstract class IStreamRequest implements Action, StreamRequest
{
  public static final String nameFrom(String aPath)
  {
    if (aPath == null)
    {
      return "";
    }
    int idx = aPath.indexOf('/');
    if (idx < 0)
    {
      return aPath;
    }
    return aPath.substring(0, idx);
  }

  public static final String subPathFrom(String aPath)
  {
    if (aPath == null)
    {
      return "";
    }
    int idx = aPath.indexOf('/');
    if (idx < 0)
    {
      return "";
    }
    return aPath.substring(++idx);
  }

  private final String theRequestId;
  private final Object theActor;

  public IStreamRequest(String aId, Object aActor)
  {
    theRequestId = aId;
    theActor = aActor;
  }

  @Override
  public Object getActor()
  {
    return theActor;
  }

  @Override
  public String getName()
  {
    return nameFrom(getPath());
  }

  @Override
  public abstract String getPath();

  @Override
  public abstract Reader getReader() throws IOException;

  @Override
  public String getRequestId()
  {
    return theRequestId;
  }

  @Override
  public abstract Writer getWriter() throws IOException;

  @Override
  public String toString()
  {
    return theRequestId;
  }
}
