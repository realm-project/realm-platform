package org.objectof.test.schema.person;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Person.locations.location")
public interface Location extends Resource<Composite>
{
  @net.objectof.Selector("latitude")
  public Double getLatitude();

  @net.objectof.Selector("latitude:")
  public void setLatitude(Double a);
  @net.objectof.Selector("longitude")
  public Double getLongitude();

  @net.objectof.Selector("longitude:")
  public void setLongitude(Double a);
  @net.objectof.Selector("geo")
  public Geo getGeo();

  @net.objectof.Selector("geo:")
  public void setGeo(Geo a);

}
