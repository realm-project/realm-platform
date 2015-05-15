package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IAssignment")
public class IAssignmentBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Assignment
{
  public IAssignmentBean(net.objectof.model.impl.IId aId)
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
  public Assignment getAssignment()
  {
    return (Assignment) _("assignment");
  }
  public void setAssignment(Assignment a)
  {
    _("assignment", a);
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
  public net.objectof.aggr.Listing<byte[]> getDocuments()
  {
    return (net.objectof.aggr.Listing<byte[]>) _("documents");
  }
  public void setDocuments(net.objectof.aggr.Listing<byte[]> a)
  {
    _("documents", a);
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
  public DeviceUI getDeviceUI()
  {
    return (DeviceUI) _("deviceUI");
  }
  public void setDeviceUI(DeviceUI a)
  {
    _("deviceUI", a);
  }

}