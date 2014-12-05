package tests;

import junit.framework.Assert;
import net.objectof.ext.Dominion;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IFn;
import net.objectof.rt.impl.base.IRuntimes;

import org.junit.Test;

public class DominionTest extends IFn
{
  @Test
  public void getRuntime()
  {
    String java = IRuntimes.RUNTIMES + '/' + IRuntimes.DEFAULT;
    Object rt = Dominion.INSTANCE.find(java);
    System.out.println(rt.toString());
    Assert.assertEquals("ans://" + java, rt.toString());
  }

  @Test
  public void getRuntimes()
  {
    // Ensure the runtimes domain is registered & default rt is loaded.
    Runtime<?> rt = runtime();
    IRuntimes rts = Dominion.INSTANCE.find(IRuntimes.RUNTIMES,
        rt.forClass(IRuntimes.class));
    System.out.println(rts.toString());
    Assert.assertEquals("ans://" + IRuntimes.RUNTIMES, rts.toString());
  }

  @Test
  public void testInvalid()
  {
    try
    {
      Dominion.INSTANCE.find("objectof.net:1401");
      Assert.fail();
    }
    catch (net.objectof.InvalidNameException e)
    {
      System.out.println(e.getMessage());
    }
  }
}
