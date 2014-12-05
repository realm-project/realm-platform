package net.objectof.impl.corc.msg;

import javax.jms.JMSException;
import javax.jms.Message;

import net.objectof.corc.Action;

public class IMsgAction implements Action
{
  public static void setMessage(Message aMessage, Action aRequest)
      throws JMSException
  {
    aMessage.setJMSCorrelationID(aRequest.getRequestId());
    aMessage.setStringProperty(REQUEST_ACTOR, aRequest.getActor().toString());
    aMessage.setStringProperty(REQUEST_NAME, aRequest.getName());
  }

  private final Message theMessage;
  public static final String REQUEST_ACTOR = "actor";
  public static final String REQUEST_NAME = "name";

  public IMsgAction(Message aMessage)
  {
    theMessage = aMessage;
  }

  @Override
  public String getActor()
  {
    try
    {
      return theMessage.getStringProperty("x");
    }
    catch (JMSException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getName()
  {
    try
    {
      return theMessage.getStringProperty("x");
    }
    catch (JMSException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getRequestId()
  {
    try
    {
      return theMessage.getJMSCorrelationID();
    }
    catch (JMSException e)
    {
      throw new RuntimeException(e);
    }
  }
}
