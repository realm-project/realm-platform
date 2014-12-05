package net.objectof.model.service.composite;
import net.objectof.model.service.*;
import net.objectof.model.Resource;

@SuppressWarnings("all")
@net.objectof.Selector("IJsStep")
public class IJsStepBean
  extends net.objectof.model.impl.aggr.IComposite
  implements JsStep
{
  public IJsStepBean(net.objectof.model.impl.IId aId)
  {
    super(aId);
  }
  public String getScript()
  {
    return (String) _("script");
  }
  public void setScript(String a)
  {
    _("script", a);
  }

}