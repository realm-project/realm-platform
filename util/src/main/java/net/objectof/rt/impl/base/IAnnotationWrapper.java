package net.objectof.rt.impl.base;

import net.objectof.facet.Annotated;
import net.objectof.facet.Property;
import net.objectof.rt.impl.IFn;

public class IAnnotationWrapper extends IFn implements Property
{
  private final Annotated theDefinedBy;
  private final String theUniqueName;
  private final Object theValue;

  public IAnnotationWrapper(Annotated aDefinedBy, String aUniqueName,
      Object aValue)
  {
    theDefinedBy = aDefinedBy;
    theUniqueName = aUniqueName;
    theValue = aValue;
  }

  @Override
  public Annotated getDefinedBy()
  {
    return theDefinedBy;
  }

  @Override
  public String getSource()
  {
    return theValue.toString();
  }

  @Override
  public String getUniqueName()
  {
    return theUniqueName;
  }
}
