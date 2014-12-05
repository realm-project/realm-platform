package net.objectof.impl.corc.requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.objectof.corc.Action;

public class ICollectingRequest extends IRequest
{
  private final String theName;
  private final Object theActor;
  private Map<String, Object> theMap;
  private final String theRequestId;

  public ICollectingRequest(IRequestor<?> aCollector, String aRequestId,
      String aName, Object aActor)
  {
    super(aCollector);
    theRequestId = aRequestId;
    theName = aName;
    theActor = aActor;
  }

  @Override
  public Class<? extends Object> getArgumentClass()
  {
    return Object.class;
  }

  @Override
  public Object getActor()
  {
    return theActor;
  }

  public Set<String> getEntries()
  {
    if (theMap == null) return Collections.emptySet();
    return theMap.keySet();
  }

  @Override
  public String getName()
  {
    return theName;
  }

  @Override
  public String getRequestId()
  {
    return theRequestId;
  }

  public Object getResponse(String aName)
  {
    return theMap == null ? null : theMap.get(aName);
  }

  @Override
  public Object setResponse(Action aResponse, Object aObject)
  {
    if (theMap == null)
    {
      theMap = new HashMap<String, Object>();
    }
    return theMap.put(aResponse.getName(), aObject);
  }

  @Override
  public String toString()
  {
    return super.toString() + (theMap == null ? "" : theMap.toString());
  }
}
