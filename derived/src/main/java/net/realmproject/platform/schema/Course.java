package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Course")
public interface Course extends Resource<Composite>
{
  @net.objectof.Selector("course")
  public Course getCourse();

  @net.objectof.Selector("course:")
  public void setCourse(Course a);
  @net.objectof.Selector("id")
  public String getId();

  @net.objectof.Selector("id:")
  public void setId(String a);
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("description")
  public String getDescription();

  @net.objectof.Selector("description:")
  public void setDescription(String a);
  @net.objectof.Selector("teachers")
  public net.objectof.aggr.Listing<Person> getTeachers();

  @net.objectof.Selector("teachers:")
  public void setTeachers(net.objectof.aggr.Listing<Person> a);
  @net.objectof.Selector("startDate")
  public java.util.Date getStartDate();

  @net.objectof.Selector("startDate:")
  public void setStartDate(java.util.Date a);
  @net.objectof.Selector("endDate")
  public java.util.Date getEndDate();

  @net.objectof.Selector("endDate:")
  public void setEndDate(java.util.Date a);

}
