package net.objectof.model.service;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Category")
public interface Category extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  //name is public

}
