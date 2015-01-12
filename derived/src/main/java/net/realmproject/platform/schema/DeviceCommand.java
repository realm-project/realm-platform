package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("DeviceCommand")
public interface DeviceCommand extends Resource<Composite>
{
  @net.objectof.Selector("device")
  public Device getDevice();

  @net.objectof.Selector("device:")
  public void setDevice(Device a);
  @net.objectof.Selector("command")
  public DeviceIO getCommand();

  @net.objectof.Selector("command:")
  public void setCommand(DeviceIO a);
  @net.objectof.Selector("states")
  public net.objectof.aggr.Listing<DeviceIO> getStates();

  @net.objectof.Selector("states:")
  public void setStates(net.objectof.aggr.Listing<DeviceIO> a);

}
