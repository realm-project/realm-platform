package net.objectof.facet.impl;

import net.objectof.Type;
import net.objectof.facet.Facet;
import net.objectof.facet.Property;

public abstract class IAttribute<T> implements Type
{
  public abstract T convert(Property aProperty);

  public abstract Facet<?> getFacet();

  public abstract String getName();

  public abstract boolean isValid(Property aProperty);

  public abstract String normalize(String aValue);
}
