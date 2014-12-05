package net.objectof.model.service;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Step")
public interface Step extends Resource<Composite>
{
  @net.objectof.Selector("category")
  public Category getCategory();

  //category is public
  @net.objectof.Selector("name")
  public String getName();

  //name is public
  @net.objectof.Selector("peer")
  public String getPeer();

  //peer is public

}
