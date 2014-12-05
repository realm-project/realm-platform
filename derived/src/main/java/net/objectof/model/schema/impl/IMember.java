package net.objectof.model.schema.impl;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema.members.member")
public class IMember
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Member
{
  public IMember(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public IMember(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public Category getCategory()
  {
    return (Category) value().get("category");
  }
  public void setCategory(Category a)
  {
    value().set("category", a);
  }
  public String getPathname()
  {
    return (String) value().get("pathname");
  }
  public void setPathname(String a)
  {
    value().set("pathname", a);
  }
  public Long getIndex()
  {
    return (Long) value().get("index");
  }
  public void setIndex(Long a)
  {
    value().set("index", a);
  }
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property> getProperties()
  {
    return (net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property>) value().get("properties");
  }
  public void setProperties(net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property> a)
  {
    value().set("properties", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}