package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Station")
public interface Station extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("devices")
  public net.objectof.aggr.Mapping<String,Device> getDevices();

  @net.objectof.Selector("devices:")
  public void setDevices(net.objectof.aggr.Mapping<String,Device> a);
  @net.objectof.Selector("owner")
  public Person getOwner();

  @net.objectof.Selector("owner:")
  public void setOwner(Person a);

}
