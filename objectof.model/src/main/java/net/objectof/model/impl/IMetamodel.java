package net.objectof.model.impl;

import java.util.Map;

import net.objectof.facet.impl.IFacets;
import net.objectof.model.Stereotype;

public abstract class IMetamodel extends IFacets<IKind<?>>
{
  private Map<Stereotype, IDatatype<?>> theDatatypes;

  public IMetamodel(String aUniqueName)
  {
    super(aUniqueName);
  }

  public IDatatype<?> forStereotype(Stereotype aStereotype)
  {
    if (theDatatypes == null)
    {
      theDatatypes = initDatatypes();
    }
    return theDatatypes.get(aStereotype);
  }

  protected abstract Map<Stereotype, IDatatype<?>> initDatatypes();
}
