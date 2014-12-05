package net.realmproject.model.schema.impl;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("IPerson")
public class IPerson
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Person
{
  public IPerson(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public IPerson(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public String getName()
  {
    return (String) value().get("name");
  }
  public void setName(String a)
  {
    value().set("name", a);
  }
  public String getEmail()
  {
    return (String) value().get("email");
  }
  public void setEmail(String a)
  {
    value().set("email", a);
  }
  public String getPwdHashed()
  {
    return (String) value().get("pwdHashed");
  }
  public void setPwdHashed(String a)
  {
    value().set("pwdHashed", a);
  }
}