package net.objectof.facet;

import net.objectof.Named;
import net.objectof.Receiver;

/**
 * An Annotated object contains a set of properties defined within some form of
 * source document. Properties can be inspected at compile time or runtime.
 *
 * @author jdh
 *
 */
public interface Annotated extends Named, Receiver
{
  // /**
  // * @param aUniqueName
  // * @return The source value of a property defined on this. Outer scopes are
  // * not checked.
  // */
  // public String getDefinedPropertyValue(String aUniqueName);
  /**
   * @param aUniqueName
   *          The unique name of the property.
   * @return The property matching the name or null when not defined.
   */
  // TODO support cascading
  // Note that the property may be defined in an Annotated object within a
  // higher/outer scope/node to this; i.e. there is a degree of
  // <em>cascading</em> of properties.
  public Property getProperty(String aUniqueName);
}
