package tests;


import java.io.FileReader;
import java.io.Reader;

import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.repo.testing.TempSQLiteRepo;

import org.junit.Test;
import org.objectof.test.schema.person.Person;


public class DeserializeTest {

    @Test
    public void test() throws Exception {
        Transaction tx = TempSQLiteRepo.testPackage().connect(this);
        Reader r = new FileReader("src/test/java/tests/RipTest.json");
        Resource<Person> person = tx.receive("application/json", r);
        StringBuilder b = new StringBuilder();
        person.send("application/json", b);
        System.out.println(b);
        tx.post();
    }
}
