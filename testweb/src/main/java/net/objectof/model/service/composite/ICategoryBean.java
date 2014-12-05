package net.objectof.model.service.composite;
import net.objectof.model.service.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ICategory")
public class ICategoryBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Category
{
  public ICategoryBean(net.objectof.model.impl.IId aId)
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