package org.objectof.test.schema.person;

import net.objectof.model.Resource;

@net.objectof.Selector("Person.locations.location")
public interface Location
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

  public Resource<?> asResource();

}
