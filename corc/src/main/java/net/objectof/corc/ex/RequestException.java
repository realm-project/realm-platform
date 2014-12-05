package net.objectof.corc.ex;

/**
 * A RequestException provides a common Exception class for wrapping or throwing
 * Exceptions that occur within application/functional logic on a
 * <em>per request</em> basis. It is recommended that Handlers that implement
 * execute() or service() directly catch Exceptions and wrap them in this class.
 * Handlers that extend from the base IHandler implementation should simply have
 * the IHandler logic trap and handle the Exception. See HandlerException for
 * managing Exceptions thrown by framework/utility logic.
 * 
 * @author jdh
 * 
 */
public class RequestException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public RequestException(Exception aException)
  {
    super(aException);
  }

  public RequestException(String aMessage)
  {
    super(aMessage);
  }

  public RequestException(String aMessage, Throwable aException)
  {
    super(aMessage, aException);
  }
}
