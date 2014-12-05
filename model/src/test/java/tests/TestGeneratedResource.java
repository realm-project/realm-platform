package tests;

import java.io.File;

import net.objectof.aggr.Composite;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.facets.ISourcePackage;

import org.junit.Test;
import org.objectof.test.schema.person.Person;

public class TestGeneratedResource
{
  public TestGeneratedResource()
  {
  }

  /*
   * TODO Note: This test is dependent on testTemplating. It's complex to
   * eliminate it. Regressions should be fine once stable generated classes are
   * created but initial tests will likely fail. --- need to fix so that
   * resource versions can be created as well.
   */
  @Test
  public void testResource() throws Exception
  {
    File f = new File("src/test/resources/models/test.xml");
    Package pkg = new ISourcePackage(IBaseMetamodel.INSTANCE, f);
    Transaction tx = pkg.connect(this);
    Person person = tx.create("Person");
    person.setName("John");
    person.setDob(new IMoment());
    Composite val = (Composite) person;
    System.out.println(val);
  }
}
