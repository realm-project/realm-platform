package net.objectof.model.service.composite;
import net.objectof.model.service.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IStep")
public class IStepBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Step
{
  public IStepBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Category getCategory()
  {
    return (Category) _("category");
  }
  public void setCategory(Category a)
  {
    _("category", a);
  }
  public String getName()
  {
    return (String) _("name");
  }
  public void setName(String a)
  {
    _("name", a);
  }
  public String getPeer()
  {
    return (String) _("peer");
  }
  public void setPeer(String a)
  {
    _("peer", a);
  }

}