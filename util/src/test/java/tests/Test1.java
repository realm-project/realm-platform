package tests;

import junit.framework.Assert;
import net.objectof.ext.Dominion;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IFn;

import org.junit.Test;

import components.MyComponent;

public class Test1
{
  private static final String NEW_VALUE = "this is a new value.";
  private static final String VALUE = "a value.";

  private static void method(Instance f)
  {
    // Evaluate our function here.
    Assert.assertNull(f.evaluate(NEW_VALUE));
  }

  private static Runtime<?> runtime()
  {
    return IFn.getNamed(IFn.DEFAULT_RUNTIME);
  }

  @Test
  public void test()
  {
    Dominion d = Dominion.INSTANCE;
    Runtime<?> java = (Runtime<?>) d.find("rt.objectof.net:1401/java");
    System.out.println(java.forName("java.lang.Object"));
  }

  @Test
  public void test1()
  {
    String name = "components.MyComponent";
    Interface forName = runtime().forName(name);
    Interface forClass = runtime().forClass(MyComponent.class);
    Assert.assertSame(forName, forClass);
    Assert.assertEquals("ans://rt.objectof.net:1401/java/" + name,
        forClass.toString());
  }

  @Test
  public void test2()
  {
    Instance r = new MyComponent();
    r.perform("value:", VALUE);
    Assert.assertEquals(VALUE, r.perform("value"));
    Instance f = r.select("value");
    System.out.println(f.evaluate());
    Assert.assertEquals(VALUE, f.evaluate());
    // create a function from the object.
    f = r.select("value:");
    // pass it around.
    method(f);
    // the receiver has been updated within "method":
    Assert.assertEquals(NEW_VALUE, r.perform("value"));
    System.out.println(r.perform("value"));
  }

  @Test
  public void testStatic()
  {
    Instance r = new MyComponent();
    // Set the state:
    r.perform("value:", VALUE);
    // Perform the static method:
    System.out.println(r.perform("||", " with concatonated value"));
  }
}
