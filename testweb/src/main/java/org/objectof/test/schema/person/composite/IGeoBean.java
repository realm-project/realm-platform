package org.objectof.test.schema.person.composite;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IGeo")
public class IGeoBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Geo
{
  public IGeoBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getDescription()
  {
    return (String) _("description");
  }
  public void setDescription(String a)
  {
    _("description", a);
  }

}