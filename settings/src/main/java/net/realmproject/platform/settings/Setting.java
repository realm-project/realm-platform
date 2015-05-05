package net.realmproject.platform.settings;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Setting")
public interface Setting extends Resource<Composite>
{
  @net.objectof.Selector("key")
  public String getKey();

  @net.objectof.Selector("key:")
  public void setKey(String a);
  @net.objectof.Selector("value")
  public String getValue();

  @net.objectof.Selector("value:")
  public void setValue(String a);

}
