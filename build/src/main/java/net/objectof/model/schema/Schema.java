package net.objectof.model.schema;

import net.objectof.model.Resource;

@net.objectof.Selector("Schema")
public interface Schema
{
  @net.objectof.Selector("name")
  public String getName();

  //name is public
  @net.objectof.Selector("release")
  public String getRelease();

  //release is public
  @net.objectof.Selector("members")
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member> getMembers();

  //members is public

  public Resource<?> asResource();

}
