package net.objectof.model.schema.impl;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ICategory")
public class ICategory
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Category
{
  public ICategory(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public ICategory(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public String getName()
  {
    return (String) value().get("name");
  }
  public void setName(String a)
  {
    value().set("name", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}