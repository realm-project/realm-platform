package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("ISession")
public class ISessionBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Session
{
  public ISessionBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Lesson getLesson()
  {
    return (Lesson) _("lesson");
  }
  public void setLesson(Lesson a)
  {
    _("lesson", a);
  }
  public java.util.Date getStartTime()
  {
    return (java.util.Date) _("startTime");
  }
  public void setStartTime(java.util.Date a)
  {
    _("startTime", a);
  }
  public Long getDuration()
  {
    return (Long) _("duration");
  }
  public void setDuration(Long a)
  {
    _("duration", a);
  }
  public String getSessionToken()
  {
    return (String) _("sessionToken");
  }
  public void setSessionToken(String a)
  {
    _("sessionToken", a);
  }
  public Person getCoordinator()
  {
    return (Person) _("coordinator");
  }
  public void setCoordinator(Person a)
  {
    _("coordinator", a);
  }
  public net.objectof.aggr.Listing<Person> getTeam()
  {
    return (net.objectof.aggr.Listing<Person>) _("team");
  }
  public void setTeam(net.objectof.aggr.Listing<Person> a)
  {
    _("team", a);
  }
}