package net.objectof.model.schema.composite;
import net.objectof.model.schema.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("ISchema")
public class ISchemaBean
  extends net.objectof.model.impl.aggr.IComposite
  implements Schema
{
  public ISchemaBean(net.objectof.model.impl.IId aId)
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
  public String getRelease()
  {
    return (String) _("release");
  }
  public void setRelease(String a)
  {
    _("release", a);
  }
  public net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member> getMembers()
  {
    return (net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member>) _("members");
  }
  public void setMembers(net.objectof.aggr.Mapping<String,net.objectof.model.schema.Member> a)
  {
    _("members", a);
  }

}