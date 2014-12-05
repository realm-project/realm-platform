package net.objectof.facet;

import net.objectof.Named;

/**
 * An Faceted object is a Annotated object that may be a container of other
 * Annotated objects, available through getParts().
 *
 * @author jdh
 *
 */
public interface Faceted extends Annotated
{
  /**
   * @return The componentName. See {@link Named} for the syntax. A
   *         componentName is the name of the object within its context. It is
   *         an "opaque path".
   */
  public String getComponentName();

  /**
   * @return Zero or more Faceted objects that are a part of this.
   */
  public Iterable<? extends Annotated> getParts();
}
