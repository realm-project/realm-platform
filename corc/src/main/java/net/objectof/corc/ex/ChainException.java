package net.objectof.corc.ex;

/**
 * A HandlerException is to be thrown by a flow when a 'framework Exception' is
 * trapped. HandlerExceptions should not generally be thrown by application
 * logic. See ExecutionException for application/functional Exceptions.
 * 
 * @author jdh
 * 
 */
public class ChainException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public ChainException(Exception aException)
  {
    super(aException);
  }

  public ChainException(String aMessage)
  {
    super(aMessage);
  }

  public ChainException(String aMessage, Throwable aException)
  {
    super(aMessage, aException);
  }
}
