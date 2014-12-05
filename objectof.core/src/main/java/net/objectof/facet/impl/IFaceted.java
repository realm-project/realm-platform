package net.objectof.facet.impl;

import java.util.Collections;
import java.util.Map;

import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.facet.Faceted;
import net.objectof.facet.Property;
import net.objectof.rt.impl.IContext;

public class IFaceted<T extends Faceted> extends IContext<T> implements Faceted
{
  private Map<String, Property> theProperties;
  private static final Iterable<String> NO_PROPERTIES = Collections.emptyList();

  public IFaceted(String aName)
  {
    super(aName);
  }

  @Override
  public String getComponentName()
  {
    String nm = getUniqueName();
    int idx = nm.lastIndexOf('/') + 1;
    return nm.substring(idx);
  }

  @Override
  public Iterable<? extends Annotated> getParts()
  {
    return this;
  }

  @Override
  public Property getProperty(String aName)
  {
    return theProperties == null ? null : theProperties.get(aName);
  }

  public Iterable<String> getPropertyNames()
  {
    return theProperties == null ? NO_PROPERTIES : theProperties.keySet();
  }

  protected Property createProperty(Facet<?> aFacet, String aName,
      Faceted aType, String aValue)
  {
    return new IProperty(aType, aFacet, aName, aValue);
  }

  /**
   * 
   * @param aProperties
   * @throws IllegalStateException
   *           When the type already has properties defined.
   */
  protected void setProperties(Map<String, Property> aProperties)
      throws IllegalStateException
  {
    if (theProperties != null)
    {
      throw new IllegalStateException("Properties are already defined.");
    }
    theProperties = aProperties;
  }
}
