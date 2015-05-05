package net.realmproject.platform.settings.composite;
import net.realmproject.platform.settings.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISetting")
public class ISettingBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Setting
{
  public ISettingBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getKey()
  {
    return (String) _("key");
  }
  public void setKey(String a)
  {
    _("key", a);
  }
  public String getValue()
  {
    return (String) _("value");
  }
  public void setValue(String a)
  {
    _("value", a);
  }

}