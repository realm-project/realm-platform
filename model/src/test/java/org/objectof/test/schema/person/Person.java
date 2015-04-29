package org.objectof.test.schema.person;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Person")
public interface Person extends Resource<Composite>
{
  @net.objectof.Selector("empNo")
  public Long getEmpNo();

  //empNo is protected
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("dob")
  public java.util.Date getDob();

  @net.objectof.Selector("dob:")
  public void setDob(java.util.Date a);
  @net.objectof.Selector("picture")
  public byte[] getPicture();

  @net.objectof.Selector("picture:")
  public void setPicture(byte[] a);
  @net.objectof.Selector("locations")
  public net.objectof.aggr.Listing<org.objectof.test.schema.person.Location> getLocations();

  //locations is public

}
