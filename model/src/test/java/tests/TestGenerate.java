package tests;

import java.io.File;

import net.objectof.Context;
import net.objectof.impl.spring.ISpringContext;
import net.objectof.model.impl.facets.IGenerator;

import org.junit.Test;

public class TestGenerate
{
  @Test
  public void testTemplating() throws Exception
  {
    Context<IGenerator> ctx = new ISpringContext<IGenerator>(IGenerator.class,
        "TestContext.xml");
    IGenerator g = ctx.forName("generator");
    g.setRoot(new File("."));
    g.process();
  }
}
