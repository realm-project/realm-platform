package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("DeviceIO")
public interface DeviceIO extends Resource<Composite>
{
  @net.objectof.Selector("json")
  public String getJson();

  @net.objectof.Selector("json:")
  public void setJson(String a);
  @net.objectof.Selector("unixtime")
  public Long getUnixtime();

  @net.objectof.Selector("unixtime:")
  public void setUnixtime(Long a);

}
