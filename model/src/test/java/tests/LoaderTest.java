package tests;

import java.io.File;

import net.objectof.model.Transaction;
import net.objectof.model.impl.IBaseMetamodel;
import net.objectof.model.impl.ILoader;
import net.objectof.model.impl.facets.ISourcePackage;

import org.junit.Test;

public class LoaderTest
{
  @Test
  public void testLoader()
  {
    Transaction t = new ISourcePackage(IBaseMetamodel.INSTANCE, new File(
        "src/test/resources/models/test.xml")).connect(this);
    ILoader loader = new ILoader(t);
    int count = loader.load("src/test/resources/loadPerson.xml");
    System.out.println("Loaded " + count + " instance/s.");
    for (Object object : loader)
    {
      System.out.println(object);
    }
  }
}
