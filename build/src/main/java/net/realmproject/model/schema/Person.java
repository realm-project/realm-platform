package net.realmproject.model.schema;

@net.objectof.Selector("Person")
public interface Person
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
}
