package net.objectof.facet;

import net.objectof.Named;
import net.objectof.Receiver;

/**
 * A Property defines a piece of meta data. A Property is similar in intent to
 * an annotation.
 * 
 * @author jdh
 * 
 */
public interface Property extends Named, Receiver
{
  /**
   * @return The named object defining this property.
   */
  public Annotated getDefinedBy();

  /**
   * @return The property value as specified in a source document.
   */
  public String getSource();
}
