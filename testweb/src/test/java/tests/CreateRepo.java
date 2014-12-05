package tests;

import java.io.File;

import net.objectof.model.Package;
import net.objectof.repo.impl.rip.IRip;
import net.objectof.repo.impl.sql.ISqlDb;

/**
 * Creates a SqlRepo (a Rip is an instance of a SqlRepo; for creation purposes
 * they are the same thing).
 *
 * @author jdh
 *
 */
public class CreateRepo
{
  final static String repositoryName = "test.objectof.org:1407/test/person";
  private static final String SCH = "../objectof.derived/src/main/resources/packages/test.xml";

  public static void main(String[] args)
  {
    ISqlDb db = new ISqlDb("testDatabase");
    Package repo = db.create(repositoryName, IRip.class, new File(SCH));
    System.out.println(repo.getUniqueName());
  }
}
