package net.objectof.model.schema.impl;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema.members.member.properties.property")
public class IProperty
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Property
{
  public IProperty(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public IProperty(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public String getId()
  {
    return (String) value().get("id");
  }
  public void setId(String a)
  {
    value().set("id", a);
  }
  public String getSource()
  {
    return (String) value().get("source");
  }
  public void setSource(String a)
  {
    value().set("source", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}