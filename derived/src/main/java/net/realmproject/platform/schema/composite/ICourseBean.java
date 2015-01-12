package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ICourse")
public class ICourseBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Course
{
  public ICourseBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Course getCourse()
  {
    return (Course) _("course");
  }
  public void setCourse(Course a)
  {
    _("course", a);
  }
  public String getId()
  {
    return (String) _("id");
  }
  public void setId(String a)
  {
    _("id", a);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }
  public String getDescription()
  {
    return (String) _("description");
  }
  public void setDescription(String a)
  {
    _("description", a);
  }
  public net.objectof.aggr.Listing<Person> getTeachers()
  {
    return (net.objectof.aggr.Listing<Person>) _("teachers");
  }
  public void setTeachers(net.objectof.aggr.Listing<Person> a)
  {
    _("teachers", a);
  }
  public java.util.Date getStartDate()
  {
    return (java.util.Date) _("startDate");
  }
  public void setStartDate(java.util.Date a)
  {
    _("startDate", a);
  }
  public java.util.Date getEndDate()
  {
    return (java.util.Date) _("endDate");
  }
  public void setEndDate(java.util.Date a)
  {
    _("endDate", a);
  }

}