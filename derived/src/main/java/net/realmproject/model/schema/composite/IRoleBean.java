package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IRole")
public class IRoleBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Role
{
  public IRoleBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }

}