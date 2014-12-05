package net.objectof.facet;

import net.objectof.Named;

/**
 * A Facet defines a set of attributes that can be associated to a Faceted
 * object though Property objects and provides the ability to process the
 * Faceted object at compile time. The general approach for Processing is to
 * generate zero or more artifacts through templating of the metadata.
 * 
 * @author jdh
 * 
 * @param <T>
 *          The object type associated to this facet. Generally T will be a
 *          Faceted.
 */
public interface Facet<T extends Annotated> extends Named
{
  public Object process(Annotated aObject, String aSelector);

  /**
   * Process an object.
   * 
   * @param aObject
   * @return A Facet-specific return value.
   * @throws Exception
   */
  public Object process(T aObject) throws Exception;
}
