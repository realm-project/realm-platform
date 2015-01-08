package net.realmproject.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Device")
public interface Device extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("owner")
  public Person getOwner();

  @net.objectof.Selector("owner:")
  public void setOwner(Person a);
  @net.objectof.Selector("sharers")
  public net.objectof.aggr.Listing<Person> getSharers();

  @net.objectof.Selector("sharers:")
  public void setSharers(net.objectof.aggr.Listing<Person> a);
  @net.objectof.Selector("deviceUIs")
  public net.objectof.aggr.Listing<DeviceUI> getDeviceUIs();

  @net.objectof.Selector("deviceUIs:")
  public void setDeviceUIs(net.objectof.aggr.Listing<DeviceUI> a);

}
