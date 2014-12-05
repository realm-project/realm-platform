package net.objectof.model.schema.impl;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema")
public class ISchema
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Schema
{
  public ISchema(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public ISchema(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
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
  public String getRelease()
  {
    return (String) value().get("release");
  }
  public void setRelease(String a)
  {
    value().set("release", a);
  }
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member> getMembers()
  {
    return (net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member>) value().get("members");
  }
  public void setMembers(net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member> a)
  {
    value().set("members", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}