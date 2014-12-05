package net.objectof.model.schema;

import net.objectof.model.Resource;

@net.objectof.Selector("Category")
public interface Category
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);

  public Resource<?> asResource();

}
