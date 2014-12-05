package org.objectof.test.schema.person.composite;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

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
  public Long getEmpNo()
  {
    return (Long) _("empNo");
  }
  public void setEmpNo(Long a)
  {
    _("empNo", a);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }
  public java.util.Date getDob()
  {
    return (java.util.Date) _("dob");
  }
  public void setDob(java.util.Date a)
  {
    _("dob", a);
  }
  public Void getPicture()
  {
    return (Void) _("picture");
  }
  public void setPicture(Void a)
  {
    _("picture", a);
  }
  public net.objectof.aggr.Listing<org.objectof.test.schema.person.Location> getLocations()
  {
    return (net.objectof.aggr.Listing<org.objectof.test.schema.person.Location>) _("locations");
  }
  public void setLocations(net.objectof.aggr.Listing<org.objectof.test.schema.person.Location> a)
  {
    _("locations", a);
  }

}