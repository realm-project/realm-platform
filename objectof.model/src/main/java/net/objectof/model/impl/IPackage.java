package net.objectof.model.impl;

import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.facet.Property;
import net.objectof.facet.impl.IProperty;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Locator;
import net.objectof.model.Package;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;
import net.objectof.rt.impl.IContext;

public abstract class IPackage extends IContext<Kind<?>> implements Package,
Locator
{
  public IPackage(String aUniqueName)
  {
    super(aUniqueName);
  }

  @Override
  public Transaction connect(Object aActor)
  {
    return new ITransaction(this, aActor);
  }

  @Override
  public String getComponentName()
  {
    return runtime().getPackageName(getUniqueName());
  }

  public <T> IDatatype<T> getDatatype(Stereotype aStereotype)
  {
    @SuppressWarnings("unchecked")
    IDatatype<T> datatype = (IDatatype<T>) getMetamodel().forStereotype(
        aStereotype);
    return datatype;
  }

  public String getLocation()
  {
    return runtime().getLocation(getUniqueName());
  }

  public abstract IMetamodel getMetamodel();

  @Override
  public Iterable<? extends Kind<?>> getParts()
  {
    return this;
  }

  @Override
  public Property getProperty(String aAbsoluteName)
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }

  public String getTitle()
  {
    return getUniqueName();
  }

  @Override
  public String locate(Id<?> aId)
  {
    Kind<?> kind = aId.kind();
    String label = aId.label().toString();
    String loc = "";
    if (kind.getPackage() != this)
    {
      loc = locate(kind.getPackage()) + '/';
    }
    StringBuilder b = new StringBuilder();
    b.append(loc).append(kind.getComponentName()).append('-')
    .append(label.toString());
    return b.toString();
  }

  protected Property createProperty(String aNamespace, String aName,
      Kind<?> aType, String aValue)
  {
    Facet<?> f = getMetamodel().fetch(aNamespace);
    return new IProperty(aType, f, aName, aValue);
  }

  protected Stereotype getStereotype(String aName)
  {
    try
    {
      return Stereotype.valueOf(aName.toUpperCase());
    }
    catch (IllegalArgumentException e)
    {
      return null;
    }
  }

  protected String locate(Package aPackage)
  {
    return "ans://" + aPackage.getUniqueName();
  }
}
