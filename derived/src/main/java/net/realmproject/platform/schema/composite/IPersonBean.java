package net.realmproject.platform.schema.composite;
import net.realmproject.platform.schema.*;
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
  public String getSalt()
  {
    return (String) _("salt");
  }
  public void setSalt(String a)
  {
    _("salt", a);
  }
  public net.objectof.aggr.Listing<Session> getSessions()
  {
    return (net.objectof.aggr.Listing<Session>) _("sessions");
  }
  public void setSessions(net.objectof.aggr.Listing<Session> a)
  {
    _("sessions", a);
  }
  public Role getRole()
  {
    return (Role) _("role");
  }
  public void setRole(Role a)
  {
    _("role", a);
  }
  public net.objectof.aggr.Listing<Course> getEnroledCourses()
  {
    return (net.objectof.aggr.Listing<Course>) _("enroledCourses");
  }
  public void setEnroledCourses(net.objectof.aggr.Listing<Course> a)
  {
    _("enroledCourses", a);
  }
  public net.objectof.aggr.Listing<Course> getPendingCourses()
  {
    return (net.objectof.aggr.Listing<Course>) _("pendingCourses");
  }
  public void setPendingCourses(net.objectof.aggr.Listing<Course> a)
  {
    _("pendingCourses", a);
  }

}