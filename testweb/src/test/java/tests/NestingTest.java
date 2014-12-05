package tests;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import net.objectof.aggr.Aggregate;
import net.objectof.aggr.Composite;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.IPackage;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.repo.impl.sql.ISqlDb;

import org.junit.Test;
import org.objectof.test.schema.person.Geo;
import org.objectof.test.schema.person.Location;
import org.objectof.test.schema.person.Person;
import org.objectof.test.schema.person.composite.IPersonBean;

public class NestingTest
{
  public static void print(Transaction aTx, Object aObject) throws IOException
  {
    @SuppressWarnings("unchecked")
    Resource<Object> o = (Resource<Object>) aObject;
    IKind<Object> kind = (IKind<Object>) o.id().kind();
    Writer w = new OutputStreamWriter(System.out);
    kind.datatype().toJson(o.value(), (IPackage) aTx.getPackage(), w);
    w.write('\n');
    w.flush();
  }

  public static void print(Transaction aTx, Object aObject, String outputFile)
      throws IOException
  {
    @SuppressWarnings("unchecked")
    Resource<Object> o = (Resource<Object>) aObject;
    IKind<Object> kind = (IKind<Object>) o.id().kind();
    OutputStream outputStream = new FileOutputStream(outputFile, true);
    Writer w = new OutputStreamWriter(outputStream);
    kind.datatype().toJson(o.value(), (IPackage) aTx.getPackage(), w);
    w.write('\n');
    w.flush();
    outputStream.close();
  }

  // @Test
  public void create() throws Exception
  {
    System.out.println("create");
    createGeos();
    createbulk(10);
  }

  @Test
  public void init() throws Exception
  {
    System.out.println("init");
    Package repo = repo();
    Transaction t = repo.connect("jdh");
    Object p = t.create("Person");
    print(t, p);
    t.close();
  }

  public void retrieve(int i) throws IOException
  {
    Transaction t = repo().connect(this);
    Person p = t.retrieve("Person", i + "");
    print(t, p);
    t.close();
  }

  @Test
  public void test()
  {
    Transaction t = repo().connect(this);
  }

  @Test
  public void update() throws IOException
  {
    System.out.println("update");
    Transaction t = repo().connect("jdh");
    Resource<Aggregate<Object, Object>> x = t.retrieve("Person", "1");
    Aggregate<Object, Object> val = x.value();
    val.set("empNo", 90L);
    t.post();
    t.close();
  }

  // @Test
  public void updateList() throws IOException
  {
    System.out.println("updatelist");
    String num = "8";
    Transaction t = repo().connect("jdh");
    Person person = t.retrieve("Person", num);
    List<Location> list = person.getLocations();
    print(t, list);
    list.remove(1);
    Location loc = t.create("Person.locations.location");
    list.add(loc);
    t.post();
    print(t, list);
    t.close();
    t = repo().connect(this);
    person = t.retrieve("Person", num);
    list = person.getLocations();
    print(t, list);
    t.close();
  }

  @Test
  public void useBean() throws IOException
  {
    System.out.println("usebean");
    Transaction t = repo().connect("jdh");
    Resource<Person> x = t.retrieve("Person", "1");
    Person val = x.value();
    val.setName("William Willy");
    t.post();
    System.out.println(t.getStatus());
    t.close();
  }

  protected void createbulk(int aNumber) throws Exception
  {
    // Create a transaction to do something. Identify the user performing the
    // transaction:
    Transaction t = repo().connect(this);
    for (int i = 1; i <= aNumber; i++)
    {
      System.out.println("i: " + i);
      Person person = createPerson(t, i);
      createPersonLocations(t, (IPersonBean) person);
    }
    // Now commit it all.
    System.out.println("Posting the transaction ...");
    t.post();
    // Let's look at our work.
    // for (Object instance : t)
    // {
    // System.out.println(instance);
    // }
    t.close();
  }

  protected void createGeos() throws Exception
  {
    Transaction t = repo().connect("john@objectof.org");
    t.<Geo> create("Geo").setDescription("Land");
    t.<Geo> create("Geo").setDescription("Sea");
    t.<Geo> create("Geo").setDescription("Air");
    t.post();
    t.close();
  }

  protected Person createPerson(Transaction aTransaction, long aEmpno)
  {
    Person person = aTransaction.<Resource<Person>> create("Person").value();
    // Populate scalar values for the new Instance:
    person.setName("Billy X. Willy " + aEmpno);
    // Person is generated with no setter visible on the interface.
    ((Composite) person).set("empNo", aEmpno);
    person.setDob(new IMoment());
    try
    {
      print(aTransaction, person);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return person;
  }

  protected void createPersonLocations(Transaction aTransaction,
      IPersonBean aPerson)
  {
    // Create locations
    IIndexed<Location> locations = aTransaction.create("Person.locations");
    // Attach to Person.
    aPerson.setLocations(locations);
    // Create the locations and assign it:
    // empNo adds some data variation on the dummy data
    long empNo = aPerson.getEmpNo();
    for (int j = 0; j < 4; j++)
    {
      Location loc = aTransaction.<Resource<Location>> create(
          "Person.locations.location").value();
      loc.setLatitude(1.0D / (j + 1));
      loc.setLongitude((double) j + empNo);
      Geo geo = (Geo) aTransaction.retrieve("Geo", Integer.toString(j % 3 + 1));
      loc.setGeo(geo);
      locations.add(loc);
    }
  }

  protected Package repo()
  {
    ISqlDb db = new ISqlDb("testDatabase");
    return db.getPackage("test.objectof.org:1405/test/person");
  }
}
