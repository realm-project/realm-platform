package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IDeviceUI")
public class IDeviceUIBean
  extends net.objectof.model.impl.aggr.IComposite
  implements DeviceUI
{
  public IDeviceUIBean(net.objectof.model.impl.IId aId)
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
  public String getUrl()
  {
    return (String) _("url");
  }
  public void setUrl(String a)
  {
    _("url", a);
  }

}