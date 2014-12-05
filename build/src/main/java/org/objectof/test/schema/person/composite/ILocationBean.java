package org.objectof.test.schema.person.composite;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IPerson.locations.location")
public class ILocationBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Location
{
  public ILocationBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Double getLatitude()
  {
    return (Double) _("latitude");
  }
  public void setLatitude(Double a)
  {
    _("latitude", a);
  }
  public Double getLongitude()
  {
    return (Double) _("longitude");
  }
  public void setLongitude(Double a)
  {
    _("longitude", a);
  }
  public Geo getGeo()
  {
    return (Geo) _("geo");
  }
  public void setGeo(Geo a)
  {
    _("geo", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}