package net.objectof.model;

/**
 * A ResourceException is thrown when there is an underlying exception related
 * to a computing resource. Generally used to wrap IOException, SQLException,
 * etc.
 * <p>
 * A ResourceException should only be thrown in the context of
 * Resource.getValue() which is intended to be used to active the resource from
 * the manager when needed.
 * 
 * @author jdh
 * 
 */
public class ResourceException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  private final Resource<?> theResource;

  public ResourceException(Resource<?> aResource, Exception aException)
  {
    this(aResource, aException.getMessage(), aException);
  }

  public ResourceException(Resource<?> aResource, String aMessage)
  {
    this(aResource, aMessage, null);
  }

  public ResourceException(Resource<?> aResource, String aMessage,
      Exception aException)
  {
    super(aMessage, aException);
    theResource = aResource;
  }

  @Override
  public String getMessage()
  {
    return "ans://" + getResource().getUniqueName() + ": " + super.getMessage();
  }

  public Resource<?> getResource()
  {
    return theResource;
  }
}
