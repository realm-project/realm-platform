package net.objectof.corc.ex;

/**
 * A ConfigurationException should be thrown when configuring/initializing a
 * Handler flow. ConfigurationException should not be thrown once a Handler/flow
 * is initialized.
 * 
 * @author jdh
 * 
 */
public class ConfigurationException extends ChainException
{
  private static final long serialVersionUID = 1L;

  public ConfigurationException(Exception aCause)
  {
    super(aCause);
  }

  public ConfigurationException(String aMessage)
  {
    super(aMessage);
  }

  public ConfigurationException(String aMessage, Exception aCause)
  {
    super(aMessage, aCause);
  }
}
