package tests;


import net.objectof.model.query.parser.QueryBuilder;

import org.junit.Test;


public class QueryTest {

    @Test
    public void intEqualsTest() {
        QueryBuilder.build("int = 7", null);
    }

    @Test
    public void realEqualsTest() {
        QueryBuilder.build("real = 7.1", null);
    }

    @Test
    public void charEqualsTest() {
        QueryBuilder.build("char = \"7\"", null);
    }

    @Test
    public void boolEqualsTest() {
        QueryBuilder.build("bool = False", null);
    }

    @Test
    public void composedTest() {
        QueryBuilder.build("bool = False and int = 7 or char = \"7\"", null);
    }
}
