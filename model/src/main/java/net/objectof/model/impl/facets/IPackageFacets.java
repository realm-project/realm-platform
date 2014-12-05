package net.objectof.model.impl.facets;

import net.objectof.facet.Facet;
import net.objectof.facet.impl.IFacets;
import net.objectof.model.Package;

public class IPackageFacets extends IFacets<Package>
{
  public IPackageFacets(String aName, Iterable<Facet<Package>> aFacets)
  {
    super(aName, aFacets);
  }

  @Override
  public Object process(Package aPackage) throws Exception
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }
}
