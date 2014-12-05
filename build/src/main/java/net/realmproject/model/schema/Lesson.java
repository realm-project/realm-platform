package net.realmproject.model.schema;

@net.objectof.Selector("Lesson")
public interface Lesson
{
  @net.objectof.Selector("name")
  public String getName();

  @net.objectof.Selector("name:")
  public void setName(String a);
}
