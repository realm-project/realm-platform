package org.objectof.test.schema.person.composite;
import org.objectof.test.schema.person.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IOther")
public class IOtherBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Other
{
  public IOtherBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public Boolean getIndicator()
  {
    return (Boolean) _("indicator");
  }
  public void setIndicator(Boolean a)
  {
    _("indicator", a);
  }
  public net.objectof.aggr.Set<Geo> getObjectset()
  {
    return (net.objectof.aggr.Set<Geo>) _("objectset");
  }
  public void setObjectset(net.objectof.aggr.Set<Geo> a)
  {
    _("objectset", a);
  }

  public Resource<?> asResource()
  {
    return this;
  }

}