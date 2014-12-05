package net.objectof.rt.impl.base;

public class Ex extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  private final Object theObject;

  public Ex(Object aObject, Exception aEx)
  {
    super(aEx);
    theObject = aObject;
  }

  public Ex(Object aObject, String aMessage)
  {
    super(aMessage);
    theObject = aObject;
  }

  public Ex(Object aObject, String aMessage, Exception aEx)
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
