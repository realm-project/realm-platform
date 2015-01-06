package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;
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
  public net.objectof.aggr.Listing<Void> getDocuments()
  {
    return (net.objectof.aggr.Listing<Void>) _("documents");
  }
  public void setDocuments(net.objectof.aggr.Listing<Void> a)
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
  public net.objectof.aggr.Listing<Device> getDevices()
  {
    return (net.objectof.aggr.Listing<Device>) _("devices");
  }
  public void setDevices(net.objectof.aggr.Listing<Device> a)
  {
    _("devices", a);
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