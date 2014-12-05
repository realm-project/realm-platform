package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("IPerson")
public class IPersonBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Person
{
  public IPersonBean(net.objectof.model.impl.IId aId)
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
  public String getEmail()
  {
    return (String) _("email");
  }
  public void setEmail(String a)
  {
    _("email", a);
  }
  public String getPwdHashed()
  {
    return (String) _("pwdHashed");
  }
  public void setPwdHashed(String a)
  {
    _("pwdHashed", a);
  }
}