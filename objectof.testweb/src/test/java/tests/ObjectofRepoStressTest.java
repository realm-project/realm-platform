package tests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import net.objectof.aggr.Composite;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.Transaction.Status;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.repo.impl.sql.ISqlDb;

import org.objectof.test.schema.person.Geo;
import org.objectof.test.schema.person.Location;
import org.objectof.test.schema.person.Person;
import org.objectof.test.schema.person.composite.IPersonBean;

public class ObjectofRepoStressTest
{
  private final static String ACTOR = "Azade";
  final static String repositoryName = "test.objectof.org:1405/test/person";

  public static void main(String[] args)
  {
    ObjectofRepoStressTest repoTest = new ObjectofRepoStressTest();
    try
    {
      // Run it only once
      // repoTest.createGeos();
      // repoTest.printGeos();
      // long start = System.currentTimeMillis();
      // repoTest.addPersons();
      // long end = System.currentTimeMillis();
      // System.out.println("Time: " + ((double) end - start) / 1000 +
      // " seconds.");
//      repoTest.retrievePersons();
      // repoTest.createLocations();
      // repoTest.retrieveLocations();

//    	Test to have multiple transactions
//    	Transaction firstTransaction =
//       repoTest.repo.connect(ACTOR);
//       Transaction secondTransaction =
//       repoTest.repo.connect(ACTOR);
//      
//       System.out.println("firstTransaction status" + firstTransaction.getStatus());
//       System.out.println("secondTransaction status" + secondTransaction.getStatus());
//       
//       firstTransaction.post();
//       secondTransaction.post();
//       
//       System.out.println("firstTransaction status" + firstTransaction.getStatus());
//       System.out.println("secondTransaction status" + secondTransaction.getStatus());
//       
//       firstTransaction.close();
//       secondTransaction.close();
//       
//       System.out.println("firstTransaction status" + firstTransaction.getStatus());
//       System.out.println("secondTransaction status" + secondTransaction.getStatus());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private final Package repo;

  public ObjectofRepoStressTest()
  {
    ISqlDb db = new ISqlDb("testDatabase");
    this.repo = db.getPackage(repositoryName);
  }

  public Package getRepo()
  {
    return this.repo;
  }

  public void retrieveLocations()
  {
    ObjectofRepoStressTest repoTest = new ObjectofRepoStressTest();
    Transaction aTransaction;
    aTransaction = repoTest.repo.connect(ACTOR);
    for (int i = 1; i <= 30; i++)
    {
      Resource<Location> l = aTransaction.retrieve("Person.locations.location",
          Integer.toString(i));
      try
      {
        RipTest.print(aTransaction, l);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    aTransaction.post();
    aTransaction.close();
  }

  public void retrievePerson(Transaction t, int i)
  {
    Person p = (Person) t.retrieve("Person", Integer.toString(i));
    try
    {
      RipTest.print(t, p);
      RipTest.print(t, p, "/home/azade/RepoTestFiles/outputFile7.txt");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void retrievePersons()
  {
    ObjectofRepoStressTest repoTest = new ObjectofRepoStressTest();
    Transaction aTransaction;
    aTransaction = repoTest.repo.connect(ACTOR);
    for (int i = 1; i <= 100000; i++)
    {
      retrievePerson(aTransaction, i);
    }
    aTransaction.post();
    aTransaction.close();
  }

  protected void createGeos() throws Exception
  {
    Transaction aTransaction = repo.connect(ACTOR);
    Geo geoLand;
    Geo geoSea;
    Geo geoAir;
    geoLand = (Geo) aTransaction.create("Geo");
    geoLand.setDescription("Land");
    RipTest.print(aTransaction, geoLand);
    geoSea = (Geo) aTransaction.create("Geo");
    geoSea.setDescription("Sea");
    RipTest.print(aTransaction, geoSea);
    geoAir = (Geo) aTransaction.create("Geo");
    geoAir.setDescription("Air");
    RipTest.print(aTransaction, geoAir);
    aTransaction.post();
    aTransaction.close();
  }

  protected Location createLocation(Transaction aTransaction,
      double latitude, double longitude, String geoLabel)
  {
    // Transaction<Resource<?>> aTransaction = repo.connect(ACTOR);
    Location location = (Location) aTransaction
        .create("Person.locations.location");
    location.setLatitude(latitude);
    location.setLongitude(longitude);
    Geo geo = (Geo) aTransaction.retrieve("Geo", geoLabel);
    location.setGeo(geo);
    // try {
    // RipTest.print(aTransaction, location);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    return location;
  }

  protected void createLocations()
  {
    Transaction aTransaction = repo.connect(ACTOR);
    for (int i = 1; i <= 10; i++)
    {
      Location location = (Location) aTransaction
          .create("Person.locations.location");
      location.setLatitude(1.0D / (i + 1));
      location.setLongitude((double) i);
      Geo geo = (Geo) aTransaction.retrieve("Geo", Integer.toString(i % 3 + 1));
      location.setGeo(geo);
    }
    aTransaction.post();
    aTransaction.close();
  }

  protected void createPerson(Transaction aTransaction,
      long aEmpno, String firstName, String lastName, String dob)
      throws IOException
  {
    if (aTransaction.getStatus() != Status.OPEN)
    {
      System.out.println("Transaction is closed!");
    }
    Person person = aTransaction.<Resource<Person>>create("Person").value();
    person.setName(firstName + " " + lastName);
    ((Composite) person).set("empNo", aEmpno);
    person.setDob(new IMoment(dob));
    createPersonLocations(aTransaction, (IPersonBean) person);
    // System.out.println("Created person: ");
    // RipTest.print(aTransaction, person);
  }

  protected void createPersonLocations(Transaction aTransaction,
      IPersonBean aPerson)
  {
    @SuppressWarnings("unchecked")
    IIndexed<Location> locations = (IIndexed<Location>) aTransaction
        .create("Person.locations");
    // Transaction<Resource<?>> myTransaction = repo.connect(ACTOR);
    // @SuppressWarnings("unchecked")
    // IIndexed<Location> locations = (IIndexed<Location>) myTransaction
    // .create("Person.locations");
    // Attach to Person.
    aPerson.setLocations(locations);
    // Create the locations and assign it:
    // empNo adds some data variation on the dummy data
    long empNo = aPerson.getEmpNo();
    for (int j = 0; j < 4; j++)
    {
      Location loc = createLocation(aTransaction, 1.0D / (j + 1), (double) j
          + empNo, Integer.toString(j % 3 + 1));
      locations.add(loc);
    }
    // myTransaction.post();
    // myTransaction.close();
  }

  private void addPersons()
  {
    FileInputStream fstream = null;
    BufferedReader br = null;
    String strLine = null;
    StringTokenizer st = null;
    long empNo = 1;
    Transaction aTransaction = null;
    try
    {
      ObjectofRepoStressTest repoTest = new ObjectofRepoStressTest();
      fstream = new FileInputStream("/home/azade/RepoTestFiles/NameAndDoB.txt");
      br = new BufferedReader(new InputStreamReader(fstream));
      aTransaction = repoTest.repo.connect(ACTOR);
      while ((strLine = br.readLine()) != null)
      {
        if (strLine != null)
        {
          st = new StringTokenizer(strLine, "\t");
          String firstName = st.nextToken();
          String lastName = st.nextToken();
          String dob = st.nextToken();
          StringTokenizer dobSt = new StringTokenizer(dob, "/");
          String month = dobSt.nextToken();
          String day = dobSt.nextToken();
          String year = dobSt.nextToken();
          if (month.length() == 1)
          {
            month = "0" + month;
          }
          if (day.length() == 1)
          {
            day = "0" + day;
          }
          dob = year + "-" + month + "-" + day;
          System.out.println("Adding: empNo: " + empNo + ", first name: "
              + firstName + ", last name: " + lastName + ", DoB: " + dob);
          repoTest.createPerson(aTransaction, empNo, firstName, lastName, dob);
          empNo++;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (br != null)
        {
          br.close();
        }
        if (fstream != null)
        {
          fstream.close();
        }
        if (aTransaction != null)
        {
          aTransaction.post();
          aTransaction.close();
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  private void printGeos() throws Exception
  {
    Transaction aTransaction = repo.connect(ACTOR);
    for (int i = 1; i <= 3; i++)
    {
      RipTest.print(aTransaction,
          aTransaction.retrieve("Geo", Integer.toString(i)));
    }
  }
}
