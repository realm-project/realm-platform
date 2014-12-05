package net.objectof.model.schema.composite;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema.members.member.properties.property")
public class IPropertyBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Property
{
  public IPropertyBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getId()
  {
    return (String) _("id");
  }
  public void setId(String a)
  {
    _("id", a);
  }
  public String getSource()
  {
    return (String) _("source");
  }
  public void setSource(String a)
  {
    _("source", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}