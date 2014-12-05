package org.objectof.test.schema.person.impl;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IOther")
public class IOther
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Other
{
  public IOther(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public IOther(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public Boolean getIndicator()
  {
    return (Boolean) value().get("indicator");
  }
  public void setIndicator(Boolean a)
  {
    value().set("indicator", a);
  }
  public net.objectof.aggr.Set<Geo> getObjectset()
  {
    return (net.objectof.aggr.Set<Geo>) value().get("objectset");
  }
  public void setObjectset(net.objectof.aggr.Set<Geo> a)
  {
    value().set("objectset", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}