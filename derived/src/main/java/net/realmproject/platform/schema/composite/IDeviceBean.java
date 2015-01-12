package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IDevice")
public class IDeviceBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Device
{
  public IDeviceBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }
  public Person getOwner()
  {
    return (Person) _("owner");
  }
  public void setOwner(Person a)
  {
    _("owner", a);
  }
  public net.objectof.aggr.Listing<Person> getSharers()
  {
    return (net.objectof.aggr.Listing<Person>) _("sharers");
  }
  public void setSharers(net.objectof.aggr.Listing<Person> a)
  {
    _("sharers", a);
  }
  public net.objectof.aggr.Listing<DeviceUI> getDeviceUIs()
  {
    return (net.objectof.aggr.Listing<DeviceUI>) _("deviceUIs");
  }
  public void setDeviceUIs(net.objectof.aggr.Listing<DeviceUI> a)
  {
    _("deviceUIs", a);
  }

}