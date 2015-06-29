package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IDeviceCommand")
public class IDeviceCommandBean
  extends net.objectof.model.impl.aggr.IComposite
  implements DeviceCommand
{
  public IDeviceCommandBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Device getDevice()
  {
    return (Device) _("device");
  }
  public void setDevice(Device a)
  {
    _("device", a);
  }
  public DeviceIO getCommand()
  {
    return (DeviceIO) _("command");
  }
  public void setCommand(DeviceIO a)
  {
    _("command", a);
  }
  public net.objectof.aggr.Listing<DeviceIO> getStates()
  {
    return (net.objectof.aggr.Listing<DeviceIO>) _("states");
  }
  public void setStates(net.objectof.aggr.Listing<DeviceIO> a)
  {
    _("states", a);
  }
  public String getUuid()
  {
    return (String) _("uuid");
  }
  public void setUuid(String a)
  {
    _("uuid", a);
  }

}