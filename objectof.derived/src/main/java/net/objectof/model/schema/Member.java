package net.objectof.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Schema.members.member")
public interface Member extends Resource<Composite>
{
  @net.objectof.Selector("category")
  public Category getCategory();

  //category is public
  @net.objectof.Selector("pathname")
  public String getPathname();

  //pathname is public
  @net.objectof.Selector("index")
  public Long getIndex();

  //index is public
  @net.objectof.Selector("properties")
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property> getProperties();

  //properties is public

}
