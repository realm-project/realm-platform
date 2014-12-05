package net.objectof.model;


public class RepositoryException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  private final Object theObject;

  public RepositoryException(Object aObject, Exception aEx)
  {
    this(aObject, aEx.getMessage(), aEx);
  }

  public RepositoryException(Object aObject, String aMessage)
  {
    this(aObject, aMessage, null);
  }

  public RepositoryException(Object aObject, String aMessage, Exception aEx)
  {
    super(aMessage, aEx);
    theObject = aObject;
  }

  @Override
  public String getMessage()
  {
    return getObject() + ": " + super.getMessage();
  }

  public Object getObject()
  {
    return theObject;
  }
}
