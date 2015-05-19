package net.realmproject.platform.schema;

import net.objectof.model.Resource;
import net.objectof.aggr.Composite;

@net.objectof.Selector("Person")
public interface Person extends Resource<Composite>
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
  @net.objectof.Selector("email")
  public String getEmail();

  @net.objectof.Selector("email:")
  public void setEmail(String a);
  @net.objectof.Selector("pwdHashed")
  public String getPwdHashed();

  @net.objectof.Selector("pwdHashed:")
  public void setPwdHashed(String a);
  @net.objectof.Selector("salt")
  public String getSalt();

  @net.objectof.Selector("salt:")
  public void setSalt(String a);
  @net.objectof.Selector("sessions")
  public net.objectof.aggr.Listing<Session> getSessions();

  @net.objectof.Selector("sessions:")
  public void setSessions(net.objectof.aggr.Listing<Session> a);
  @net.objectof.Selector("role")
  public Role getRole();

  @net.objectof.Selector("role:")
  public void setRole(Role a);
  @net.objectof.Selector("enrolledCourses")
  public net.objectof.aggr.Listing<Course> getEnrolledCourses();

  @net.objectof.Selector("enrolledCourses:")
  public void setEnrolledCourses(net.objectof.aggr.Listing<Course> a);
  @net.objectof.Selector("pendingCourses")
  public net.objectof.aggr.Listing<Course> getPendingCourses();

  @net.objectof.Selector("pendingCourses:")
  public void setPendingCourses(net.objectof.aggr.Listing<Course> a);

}
