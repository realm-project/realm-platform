package tests;

import junit.framework.TestCase;
import net.objectof.rt.Interface;
import net.objectof.rt.impl.IFn;
import net.objectof.rt.impl.IInterface;

import org.junit.Test;

public class ITypedTest extends TestCase
{
  @Test
  public void test1()
  {
    IFn object = new IFn();
    Interface type = object.type();
    String typeName = type.getUniqueName();
    Class<?> cl = ((IInterface) type).peer();
    assertEquals(IFn.class, cl);
    assertEquals("rt.objectof.net:1401/java/net.objectof.rt.impl.IFn", typeName);
  }
}
