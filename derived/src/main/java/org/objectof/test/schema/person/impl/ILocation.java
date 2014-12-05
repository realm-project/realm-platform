package org.objectof.test.schema.person.impl;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IPerson.locations.location")
public class ILocation
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Location
{
  public ILocation(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public ILocation(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public Double getLatitude()
  {
    return (Double) value().get("latitude");
  }
  public void setLatitude(Double a)
  {
    value().set("latitude", a);
  }
  public Double getLongitude()
  {
    return (Double) value().get("longitude");
  }
  public void setLongitude(Double a)
  {
    value().set("longitude", a);
  }
  public Geo getGeo()
  {
    return (Geo) value().get("geo");
  }
  public void setGeo(Geo a)
  {
    value().set("geo", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}