package net.objectof.model;

import net.objectof.Named;

/**
 * A Resource is a Named reference to an object. The resource can be a reference
 * to an object (value() returns a different object) or the object itself by
 * returning "this" for value(). Resource that return "this" for value() are
 * termed <em>Components</em>.
 * <p>
 * (TENTATIVE) The equality of Resource is determined by:
 * {@code this.id().equals(a.id()) && this.version().equals(a.version())}
 * 
 * @author jdh
 * 
 */
public interface Resource<T> extends Named
{
  /**
   * @return The Resource Id.
   */
  public Id<T> id();

  /**
   * Marshals the resource into the given media type, to the given stream.
   * 
   * @param aMediaType
   *          The format of the data stream
   * @param aStream
   */
  public void send(String aMediaType, Appendable aStream);

  /**
   * Obtain the resource value. Implementors are free to implement a lazy
   * pattern to return the resource, i.e. a Resource can defer accessing the
   * associated resource manager until this method is called. Whether the
   * resource obtained is current or aged (e.g. through being cached) is not
   * defined by this interface however sub-interfaces or concrete
   * implementations may refine the contract.
   * 
   * @return The resource itself.
   */
  public T value() throws ResourceException;

  /**
   * @return The version of the resource or null when the resource is not (yet)
   *         persistent. Sub-interfaces may define a more specific return type
   *         to provide additional status on the resource or this reference to
   *         it.
   */
  public Object version() throws ResourceException;
  
  
  
  /**
   * @return the {@link Transaction} which loaded this object
   */
  public Transaction tx();
}
