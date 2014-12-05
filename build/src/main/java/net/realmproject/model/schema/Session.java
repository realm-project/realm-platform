package net.realmproject.model.schema;

@net.objectof.Selector("Session")
public interface Session
{
  @net.objectof.Selector("lesson")
  public Lesson getLesson();

  @net.objectof.Selector("lesson:")
  public void setLesson(Lesson a);
  @net.objectof.Selector("startTime")
  public java.util.Date getStartTime();

  @net.objectof.Selector("startTime:")
  public void setStartTime(java.util.Date a);
  @net.objectof.Selector("duration")
  public Long getDuration();

  @net.objectof.Selector("duration:")
  public void setDuration(Long a);
  @net.objectof.Selector("sessionToken")
  public String getSessionToken();

  @net.objectof.Selector("sessionToken:")
  public void setSessionToken(String a);
  @net.objectof.Selector("coordinator")
  public Person getCoordinator();

  @net.objectof.Selector("coordinator:")
  public void setCoordinator(Person a);
  @net.objectof.Selector("team")
  public net.objectof.aggr.Listing<Person> getTeam();

  @net.objectof.Selector("team:")
  public void setTeam(net.objectof.aggr.Listing<Person> a);
}
