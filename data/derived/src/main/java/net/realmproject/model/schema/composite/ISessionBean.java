package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISession")
public class ISessionBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Session
{
  public ISessionBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Assignment getAssignment()
  {
    return (Assignment) _("assignment");
  }
  public void setAssignment(Assignment a)
  {
    _("assignment", a);
  }
  public java.util.Date getStartTime()
  {
    return (java.util.Date) _("startTime");
  }
  public void setStartTime(java.util.Date a)
  {
    _("startTime", a);
  }
  public Long getDuration()
  {
    return (Long) _("duration");
  }
  public void setDuration(Long a)
  {
    _("duration", a);
  }
  public String getSessionToken()
  {
    return (String) _("sessionToken");
  }
  public void setSessionToken(String a)
  {
    _("sessionToken", a);
  }
  public net.objectof.aggr.Mapping<String,Device> getDevices()
  {
    return (net.objectof.aggr.Mapping<String,Device>) _("devices");
  }
  public void setDevices(net.objectof.aggr.Mapping<String,Device> a)
  {
    _("devices", a);
  }
  public net.objectof.aggr.Listing<DeviceCommand> getCommands()
  {
    return (net.objectof.aggr.Listing<DeviceCommand>) _("commands");
  }
  public void setCommands(net.objectof.aggr.Listing<DeviceCommand> a)
  {
    _("commands", a);
  }

}