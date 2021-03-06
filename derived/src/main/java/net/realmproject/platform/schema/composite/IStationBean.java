package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IStation")
public class IStationBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Station
{
  public IStationBean(net.objectof.model.impl.IId aId)
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
  public net.objectof.aggr.Mapping<String,Device> getDevices()
  {
    return (net.objectof.aggr.Mapping<String,Device>) _("devices");
  }
  public void setDevices(net.objectof.aggr.Mapping<String,Device> a)
  {
    _("devices", a);
  }
  public Person getOwner()
  {
    return (Person) _("owner");
  }
  public void setOwner(Person a)
  {
    _("owner", a);
  }

}