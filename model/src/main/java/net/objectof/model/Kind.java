package net.objectof.model;

import net.objectof.Selector;
import net.objectof.Type;
import net.objectof.aggr.Listing;
import net.objectof.facet.Annotated;
import net.objectof.facet.Faceted;

/**
 * A Kind is a Faceted Type that models a data value or data structure.
 * 
 * @author jdh
 * 
 * @param <T>
 */
@Selector
public interface Kind<T> extends Faceted, Type
{
  public Package getPackage();

  /**
   * @return the owning structure when this type is-part-of an aggregate type.
   *         Return null when this is a whole entity.
   */
  @Selector("partOf")
  public Kind<?> getPartOf();

  @Override
	public Listing<? extends Kind<?>> getParts();
  
  /**
   * @return The Stereotype of this type.
   */
  public Stereotype getStereotype();
}
