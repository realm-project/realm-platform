package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IDeviceIO")
public class IDeviceIOBean
  extends net.objectof.model.impl.aggr.IComposite
  implements DeviceIO
{
  public IDeviceIOBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getJson()
  {
    return (String) _("json");
  }
  public void setJson(String a)
  {
    _("json", a);
  }
  public Long getUnixtime()
  {
    return (Long) _("unixtime");
  }
  public void setUnixtime(Long a)
  {
    _("unixtime", a);
  }

}