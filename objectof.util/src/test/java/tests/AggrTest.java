package tests;

import junit.framework.Assert;
import net.objectof.aggr.impl.IArray;
import net.objectof.rt.impl.IInterface;

import org.junit.Test;

public class AggrTest
{
  @Test
  public void test1()
  {
    IArray<String> arr = new IArray<>("Hello", "world");
    System.out.println(arr.evaluate(1));
  }

  @Test
  public void test2()
  {
    IArray<?> arr = new IArray<>("Hello", "world");
    StringBuilder b = new StringBuilder();
    IInterface.buildSignature(b, arr.type());
    System.out.println(b);
    @SuppressWarnings("unchecked")
    IArray<Object> x = (IArray<Object>) arr;
    x.set(0, "World");
    try
    {
      x.set(1, (Object) 32);
      Assert.fail();
    }
    catch (IllegalArgumentException e)
    {
      Assert.assertTrue(e.getMessage().startsWith("Value must be a 'ans://"));
    }
    System.out.println(arr.evaluate(1));
  }

  @Test
  public void test3()
  {
    IArray<?> arr = new IArray<>("Hello", "world");
    IInterface type = (IInterface) arr.type();
    StringBuilder b = new StringBuilder();
    IInterface.buildSignature(b, type);
    System.out.println(b);
  }
}
