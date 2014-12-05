package components;

import net.objectof.Selector;
import net.objectof.rt.impl.IFn;

@Selector
public class MyComponent extends IFn
{
  @Selector("||")
  public static String concat(MyComponent aReceiver, Object aObject)
  {
    return aReceiver.getValue() + aObject;
  }

  private String theValue = "";

  @Selector("value")
  public String getValue()
  {
    return theValue;
  }

  @Selector("value:")
  public void setValue(String aValue)
  {
    theValue = aValue;
  }
}
