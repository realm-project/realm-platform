package net.objectof.impl.corc.msg;

import javax.jms.Message;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.Service;
import net.objectof.impl.corc.IHandler;

public class IMsgAdaptor extends IHandler<Message>
{
  private final Service<Message, Object> theUnmarshaller;

  public IMsgAdaptor(Handler<Object> aDefaultChannelHandler,
      Service<Message, Object> aUnmarshaller)
  {
    super(aDefaultChannelHandler);
    theUnmarshaller = aUnmarshaller;
  }

  @Override
  public Class<? extends Message> getArgumentClass()
  {
    return Message.class;
  }

  public void onMessage(Message aMessage)
  {
    Action action = new IMsgAction(aMessage);
    execute(action, aMessage);
  }

  @Override
  protected void onExecute(Action aRequest, Message aMessage) throws Exception
  {
    Object object = theUnmarshaller.service(aRequest, aMessage);
    chain(aRequest, object);
  }
}
