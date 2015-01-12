package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Assignment")
public interface Assignment extends Resource<Composite>
{
  @net.objectof.Selector("course")
  public Course getCourse();

  @net.objectof.Selector("course:")
  public void setCourse(Course a);
  @net.objectof.Selector("assignment")
  public Assignment getAssignment();

  @net.objectof.Selector("assignment:")
  public void setAssignment(Assignment a);
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("description")
  public String getDescription();

  @net.objectof.Selector("description:")
  public void setDescription(String a);
  @net.objectof.Selector("documents")
  public net.objectof.aggr.Listing<Void> getDocuments();

  @net.objectof.Selector("documents:")
  public void setDocuments(net.objectof.aggr.Listing<Void> a);
  @net.objectof.Selector("startDate")
  public java.util.Date getStartDate();

  @net.objectof.Selector("startDate:")
  public void setStartDate(java.util.Date a);
  @net.objectof.Selector("endDate")
  public java.util.Date getEndDate();

  @net.objectof.Selector("endDate:")
  public void setEndDate(java.util.Date a);
  @net.objectof.Selector("deviceUI")
  public DeviceUI getDeviceUI();

  @net.objectof.Selector("deviceUI:")
  public void setDeviceUI(DeviceUI a);

}
