package net.objectof.impl.corc.util;

import java.util.Map;
import java.util.Map.Entry;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.ex.ConfigurationException;
import net.objectof.impl.corc.IUtilHandler;

/**
 * A Router selects a Handler from a Map of Handlers keyed to a channel name.
 * 
 * @author jdh
 */
public class IRouter extends IUtilHandler
{
  private final Map<String, Handler<Object>> theHandlers;

  public IRouter(Map<String, Handler<Object>> aMap)
  {
    /*
     * A Router forwards based on the Map and therefore the superclass forwarder
     * isn't used.
     */
    super(null);
    for (Entry<String, Handler<Object>> entry : aMap.entrySet())
    {
      verifyHandler(entry.getKey(), entry.getValue());
    }
    theHandlers = aMap;
  }

  @Override
  protected Handler<Object> getChain(String aName)
  {
    Handler<Object> channel = theHandlers.get(aName);
    if (channel == null)
    {
      log().debug("No Handler for '" + aName + "' found, sending to default.");
      channel = super.getChain("");
    }
    return channel;
  }

  /**
   * Return the name of a configured channel from aRequest and/or aObject. The
   * default implementation is aRequest.getName().
   * 
   * @param aRequest
   * @param aObject
   * @return The channel name to route to.
   */
  protected String getChannelName(Action aRequest, Object aObject)
  {
    return aRequest.getName();
  }

  @Override
  protected void onExecute(Action aRequest, Object aObject)
  {
    String channelName = getChannelName(aRequest, aObject);
    Handler<Object> handler = getChain(channelName);
    handler.execute(aRequest, aObject);
  }

  protected void verifyHandler(String aName, Handler<?> aHandler)
      throws ConfigurationException
  {
  }
}
