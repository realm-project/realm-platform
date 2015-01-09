package net.realmproject.model.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("DeviceUI")
public interface DeviceUI extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("url")
  public String getUrl();

  @net.objectof.Selector("url:")
  public void setUrl(String a);
  @net.objectof.Selector("layout")
  public String getLayout();

  @net.objectof.Selector("layout:")
  public void setLayout(String a);
  @net.objectof.Selector("keys")
  public net.objectof.aggr.Listing<String> getKeys();

  @net.objectof.Selector("keys:")
  public void setKeys(net.objectof.aggr.Listing<String> a);

}
