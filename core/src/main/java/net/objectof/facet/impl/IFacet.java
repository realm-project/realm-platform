package net.objectof.facet.impl;

import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.facet.Faceted;
import net.objectof.facet.Property;
import net.objectof.rt.impl.IFn;

public class IFacet<T extends Annotated> extends IFn implements Facet<T>
{
  private final String theAbsoluteName;

  public IFacet(String aAbsoluteName)
  {
    theAbsoluteName = aAbsoluteName;
  }

  public Property createProperty(Faceted aType, String aName, String aSource)
  {
    /*
     * Default is to always create a property. Processing indicates errors.
     * Override to create properties with behavior attached.
     */
    return new IProperty(aType, this, aName, aSource);
  }

  @Override
  public String getUniqueName()
  {
    return theAbsoluteName;
  }

  @Override
  public Object process(Annotated aObject, String aSelector)
  {
    return aObject.getProperty(getUniqueName() + '/' + aSelector).getSource();
  }

  @Override
  public Object process(T aObject) throws Exception
  {
    return null;
  }

  @Override
  public String toString()
  {
    return theAbsoluteName;
  }
}
