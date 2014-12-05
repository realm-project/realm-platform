package net.realmproject.model.schema.impl;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("ISession")
public class ISession
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Session
{
  public ISession(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public ISession(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
  {
    super(aType);
  }
  public Lesson getLesson()
  {
    return (Lesson) value().get("lesson");
  }
  public void setLesson(Lesson a)
  {
    value().set("lesson", a);
  }
  public java.util.Date getStartTime()
  {
    return (java.util.Date) value().get("startTime");
  }
  public void setStartTime(java.util.Date a)
  {
    value().set("startTime", a);
  }
  public Long getDuration()
  {
    return (Long) value().get("duration");
  }
  public void setDuration(Long a)
  {
    value().set("duration", a);
  }
  public String getSessionToken()
  {
    return (String) value().get("sessionToken");
  }
  public void setSessionToken(String a)
  {
    value().set("sessionToken", a);
  }
  public Person getCoordinator()
  {
    return (Person) value().get("coordinator");
  }
  public void setCoordinator(Person a)
  {
    value().set("coordinator", a);
  }
  public net.objectof.aggr.Listing<Person> getTeam()
  {
    return (net.objectof.aggr.Listing<Person>) value().get("team");
  }
  public void setTeam(net.objectof.aggr.Listing<Person> a)
  {
    value().set("team", a);
  }
}