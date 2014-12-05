package org.objectof.test.schema.person.impl;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IGeo")
public class IGeo
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Geo
{
  public IGeo(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public IGeo(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public String getDescription()
  {
    return (String) value().get("description");
  }
  public void setDescription(String a)
  {
    value().set("description", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}