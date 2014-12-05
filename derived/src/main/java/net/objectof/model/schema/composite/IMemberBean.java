package net.objectof.model.schema.composite;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema.members.member")
public class IMemberBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Member
{
  public IMemberBean(net.objectof.model.impl.IId aId)
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
  public String getPathname()
  {
    return (String) _("pathname");
  }
  public void setPathname(String a)
  {
    _("pathname", a);
  }
  public Long getIndex()
  {
    return (Long) _("index");
  }
  public void setIndex(Long a)
  {
    _("index", a);
  }
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property> getProperties()
  {
    return (net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property>) _("properties");
  }
  public void setProperties(net.objectof.aggr.Mapping<String,net.objectof.model.schema.Property> a)
  {
    _("properties", a);
  }

}