package org.objectof.test.schema.person;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Other")
public interface Other extends Resource<Composite>
{
  @net.objectof.Selector("indicator")
  public Boolean getIndicator();

  @net.objectof.Selector("indicator:")
  public void setIndicator(Boolean a);
  @net.objectof.Selector("objectset")
  public net.objectof.aggr.Set<Geo> getObjectset();

  @net.objectof.Selector("objectset:")
  public void setObjectset(net.objectof.aggr.Set<Geo> a);

}
