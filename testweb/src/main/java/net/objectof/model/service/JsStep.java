package net.objectof.model.service;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("JsStep")
public interface JsStep extends Resource<Composite>
{
  @net.objectof.Selector("script")
  public String getScript();

  @net.objectof.Selector("script:")
  public void setScript(String a);

}
