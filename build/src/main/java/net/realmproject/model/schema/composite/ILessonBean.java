package net.realmproject.model.schema.composite;
import net.realmproject.model.schema.*;

@SuppressWarnings("all")
@net.objectof.Selector("ILesson")
public class ILessonBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Lesson
{
  public ILessonBean(net.objectof.model.impl.IId aId)
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
}