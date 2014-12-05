package net.objectof.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Schema.members.member.properties.property")
public interface Property extends Resource<Composite>
{
  @net.objectof.Selector("id")
  public String getId();

  //id is public
  @net.objectof.Selector("source")
  public String getSource();

  //source is public

}
