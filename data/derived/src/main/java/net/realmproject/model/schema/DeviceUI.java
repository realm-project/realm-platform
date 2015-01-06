package net.realmproject.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("DeviceUI")
public interface DeviceUI extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("url")
  public String getUrl();

  @net.objectof.Selector("url:")
  public void setUrl(String a);

}
