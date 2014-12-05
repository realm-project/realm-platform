package net.objectof.ext;

/**
 * A Builder encapsulates construction of object graphs and trees. Builders are
 * not constrained to building Part objects however this interface is designed
 * to support them.
 * <p>
 * Builders have a concept of a "current part" to manage construction of
 * aggregate objects. The current part maintained by the start() and end()
 * events.
 * <p>
 * The name and location arguments in the methods allow any object. The name
 * generally should be a String that relates to the object's model and will
 * commonly be used to define the type of object to create. Location enables
 * referencing from the target object back to its source. Any object can be
 * specified for location however the location's toString() method should return
 * a unique value for each part within the entity being built. Location should
 * be treated as optional from the source and the builder should be prepared to
 * generate arbitrary unique locations when required by the target and not
 * provided by the source.
 *
 *
 *
 * @author jdh
 *
 */
public interface Builder<T> extends Appender<Notice>, Iterable<Notice>
{
  /**
   * Notify this builder that the current part is complete. In general terms,
   * this should logically "pop" to restore the prior current.
   *
   * @return the prior part
   */
  public T end();

  /**
   * Notify this builder that a new part should be created and attached to the
   * current part. The new part becomes the current part. A subsequent call to
   * end() by the caller is expected by this builder when the new part is
   * complete. In general terms, this should logically "push" the new part.
   *
   * @param aName
   *          The name of the part. This contract doesn't specify any
   *          constraints on the name however implementations may do so.
   * @param aLocation
   *          Optional. The source locator of the Part. aLocation.toString()
   *          should be unique relative to the whole entity. Locations provide a
   *          handle back to the location within the source and should be an id,
   *          path, offset, line/column or other handle appropriate for the
   *          source.
   * @return The new part
   */
  public T start(Object aName, Object aLocation, Object aValue);
}
