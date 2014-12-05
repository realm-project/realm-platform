package org.objectof.test.schema.person;

import net.objectof.model.Resource;

@net.objectof.Selector("Geo")
public interface Geo
{
  @net.objectof.Selector("description")
  public String getDescription();

  @net.objectof.Selector("description:")
  public void setDescription(String a);

  public Resource<?> asResource();

}
