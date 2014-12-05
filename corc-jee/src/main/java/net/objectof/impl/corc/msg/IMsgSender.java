package net.objectof.impl.corc.msg;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.ex.ConfigurationException;
import net.objectof.impl.corc.IHandler;

public abstract class IMsgSender<T> extends IHandler<T>
{
  public static final void rollback(QueueSession aSession)
  {
    if (aSession == null) return;
    try
    {
      aSession.rollback();
      aSession.close();
    }
    catch (Exception e)
    {
    }
  }

  private final QueueSession theSession;
  private final QueueSender theSender;

  public IMsgSender(Handler<Object> aForwarder, String aConnectionFactory,
      String aQueueName)
  {
    super(aForwarder);
    Queue queue = initializeQueue(aQueueName);
    QueueConnection conn = createConnection(aConnectionFactory);
    theSession = createSession(conn);
    theSender = createSender(theSession, queue);
  }

  protected QueueConnection createConnection(String aQCFactoryName)
  {
    try
    {
      InitialContext ctx = new InitialContext();
      QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx
          .lookup(aQCFactoryName);
      return connFactory.createQueueConnection();
    }
    catch (Exception e)
    {
      throw new ConfigurationException(e);
    }
  }

  protected abstract Message createMessage(Action aRequest, T aObject)
      throws Exception;

  protected QueueSender createSender(QueueSession aSession, Queue aQueue)
  {
    try
    {
      QueueSender sender = aSession.createSender(aQueue);
      sender.setDeliveryMode(getDeliveryMode());
      return sender;
    }
    catch (Exception e)
    {
      throw new ConfigurationException(e);
    }
  }

  protected QueueSession createSession(QueueConnection aQC)
  {
    try
    {
      return aQC.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
    }
    catch (Exception e)
    {
      throw new ConfigurationException(e);
    }
  }

  protected int getDeliveryMode()
  {
    return DeliveryMode.PERSISTENT;
  }

  protected Queue initializeQueue(String aQueueName)
  {
    try
    {
      InitialContext ctx = new InitialContext();
      return (Queue) ctx.lookup(aQueueName);
    }
    catch (Exception e)
    {
      throw new ConfigurationException(e);
    }
  }

  @Override
  protected final void onExecute(Action aRequest, T aObject) throws Exception
  {
    Message message = createMessage(aRequest, aObject);
    setMessageAction(message, aRequest);
    send(message);
  }

  protected void send(Message aMessage) throws Exception
  {
    theSender.send(aMessage);
  }

  protected void setMessageAction(Message aMessage, Action aAction)
      throws JMSException
  {
    IMsgAction.setMessage(aMessage, aAction);
  }
}
