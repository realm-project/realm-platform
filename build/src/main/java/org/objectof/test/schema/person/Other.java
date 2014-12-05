package org.objectof.test.schema.person;

import net.objectof.model.Resource;

@net.objectof.Selector("Other")
public interface Other
{
  @net.objectof.Selector("indicator")
  public Boolean getIndicator();

  @net.objectof.Selector("indicator:")
  public void setIndicator(Boolean a);
  @net.objectof.Selector("objectset")
  public net.objectof.aggr.Set<Geo> getObjectset();

  @net.objectof.Selector("objectset:")
  public void setObjectset(net.objectof.aggr.Set<Geo> a);

  public Resource<?> asResource();

}
