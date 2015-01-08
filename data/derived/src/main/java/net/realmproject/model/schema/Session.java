package net.realmproject.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Session")
public interface Session extends Resource<Composite>
{
  @net.objectof.Selector("assignment")
  public Assignment getAssignment();

  @net.objectof.Selector("assignment:")
  public void setAssignment(Assignment a);
  @net.objectof.Selector("startTime")
  public java.util.Date getStartTime();

  @net.objectof.Selector("startTime:")
  public void setStartTime(java.util.Date a);
  @net.objectof.Selector("duration")
  public Long getDuration();

  @net.objectof.Selector("duration:")
  public void setDuration(Long a);
  @net.objectof.Selector("sessionToken")
  public String getSessionToken();

  @net.objectof.Selector("sessionToken:")
  public void setSessionToken(String a);
  @net.objectof.Selector("commands")
  public net.objectof.aggr.Listing<DeviceCommand> getCommands();

  @net.objectof.Selector("commands:")
  public void setCommands(net.objectof.aggr.Listing<DeviceCommand> a);
  @net.objectof.Selector("station")
  public Station getStation();

  @net.objectof.Selector("station:")
  public void setStation(Station a);

}
