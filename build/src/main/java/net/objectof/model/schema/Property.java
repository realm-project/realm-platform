package net.objectof.model.schema;

import net.objectof.model.Resource;

@net.objectof.Selector("Schema.members.member.properties.property")
public interface Property
{
  @net.objectof.Selector("id")
  public String getId();

  //id is public
  @net.objectof.Selector("source")
  public String getSource();

  //source is public

  public Resource<?> asResource();

}
