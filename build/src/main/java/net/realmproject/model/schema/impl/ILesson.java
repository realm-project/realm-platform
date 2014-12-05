package net.realmproject.model.schema.impl;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("ILesson")
public class ILesson
  extends net.objectof.model.impl.facets.IResource<net.objectof.aggr.Composite>
  implements Lesson
{
  public ILesson(net.objectof.model.impl.IId<net.objectof.aggr.Composite> aIdent)
  {
    super(aIdent);
  }

  public ILesson(net.objectof.model.impl.IKind<net.objectof.aggr.Composite> aType)
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
}