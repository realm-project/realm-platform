package net.objectof.facet.impl;

import net.objectof.facet.Facet;
import net.objectof.facet.Faceted;
import net.objectof.facet.Property;
import net.objectof.rt.impl.IFn;

public class IProperty extends IFn implements Property
{
  private final Facet<?> theFacet;
  private final Faceted theObject;
  private final String theName;
  private final String theSource;

  public IProperty(Faceted aObject, Facet<?> aFacet, String aName,
      String aSource)
  {
    theObject = aObject;
    theFacet = aFacet;
    theName = aName;
    theSource = aSource;
  }

  @Override
  public Faceted getDefinedBy()
  {
    return theObject;
  }

  public Facet<?> getFacet()
  {
    return theFacet;
  }

  public String getName()
  {
    return theName;
  }

  @Override
  public String getSource()
  {
    return theSource;
  }

  @Override
  public String getUniqueName()
  {
    return theFacet.getUniqueName() + '/' + theName;
  }
}
