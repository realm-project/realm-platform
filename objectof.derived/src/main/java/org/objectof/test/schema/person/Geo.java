package org.objectof.test.schema.person;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Geo")
public interface Geo extends Resource<Composite>
{
  @net.objectof.Selector("description")
  public String getDescription();

  @net.objectof.Selector("description:")
  public void setDescription(String a);

}
