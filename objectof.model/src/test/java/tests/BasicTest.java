package tests;

import java.io.File;

import junit.framework.Assert;
import net.objectof.model.Kind;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.facets.ISourcePackage;

import org.junit.Test;
import org.objectof.test.schema.person.Person;
import org.objectof.test.schema.person.composite.IPersonBean;

public class BasicTest
{
  public final IPackage schema = new ISourcePackage(IBaseMetamodel.INSTANCE,
      new File("src/test/resources/models/test.xml"));

  @Test
  public void test() throws Exception
  {
    for (Kind<?> member : schema)
    {
      System.out.println(member.getComponentName());
      IKind<?> k = (IKind<?>) member;
      for (String s : k.getPropertyNames())
      {
        System.out.println("  " + s + ": " + member.getProperty(s));
      }
    }
  }

  @Test
  public void testTx()
  {
    Transaction Tx = schema.connect(this);
    Person p = Tx.create("Person");
    p.setName("Billy Xavier Willy");
    p.setDob(new IMoment());
    p = Tx.create("Person");
    p.setName("Frankie Hollywood");
    ((IPersonBean) p).set("empNo", 999L);
    p = Tx.retrieve("Person", "-1");
    Assert.assertEquals("Billy Xavier Willy", p.getName());
    /*
     * - NAS This testing seems to use the iterable property of transactions
     * which has been removed Transaction<Composite> casted =
     * (Transaction<Composite>) (Object) Tx; for (Composite item : casted) {
     * System.out.println(item); for (String selector : item.keySet()) {
     * System.out.println(" " + selector + " = " + item.get(selector)); } }
     */
  }
}
