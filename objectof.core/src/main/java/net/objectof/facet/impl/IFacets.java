package net.objectof.facet.impl;

import java.util.HashMap;
import java.util.Map;

import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;

public class IFacets<T extends Annotated> extends IFacet<T>
{
  private final Map<String, Facet<T>> theFacets;

  public IFacets(String aName)
  {
    super(aName);
    theFacets = new HashMap<String, Facet<T>>();
  }

  public IFacets(String aName, Iterable<Facet<T>> aFacets)
  {
    this(aName);
    for (Facet<T> facet : aFacets)
    {
      theFacets.put(facet.getUniqueName(), facet);
    }
  }

  public Iterable<? extends Facet<T>> facets()
  {
    return theFacets.values();
  }

  public Facet<T> fetch(String aNamespace)
  {
    Facet<T> f = theFacets.get(aNamespace);
    if (f == null)
    {
      f = new IFacet<T>(aNamespace);
      theFacets.put(aNamespace, f);
    }
    return f;
  }

  @Override
  public Object process(T aObject) throws Exception
  {
    StringBuilder b = new StringBuilder();
    for (Facet<T> f : theFacets.values())
    {
      Object o = f.process(aObject);
      b.append(o);
    }
    return b.toString();
  }
}
