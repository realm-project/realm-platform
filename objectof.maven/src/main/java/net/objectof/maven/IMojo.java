package net.objectof.maven;

import java.io.File;
import java.io.IOException;

import net.objectof.Context;
import net.objectof.Receiver;
//import net.objectof.Context;
import net.objectof.impl.spring.ISpringContext;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

/**
 * @goal generate
 *
 * @phase generate-sources
 */
public class IMojo implements Mojo
{
  /**
   * @parameter expression="${generate.file}"
   *
   */
  private String file;
  /**
   * @parameter expression="${project.build.directory}"
   * @readonly
   */
  private String theDirectory;
  private Log theLog;

  public IMojo()
  {
  }

  @Override
  public void execute() throws MojoExecutionException
  {
    try
    {
      File dir = defineProjectDirectory();
      getLog().info("Processing " + dir + " with " + file + " configuration.");
      Context<Receiver> ctx = defineContext();
      Receiver r = ctx.forName("generator");
      r.perform("root:", dir);
      r.perform("process");
      getLog().debug("Done.");
    }
    catch (Exception e)
    {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  @Override
  public Log getLog()
  {
    return theLog;
  }

  public void setFile(String aFile)
  {
    file = aFile;
  }

  @Override
  public void setLog(Log aLog)
  {
    theLog = aLog;
  }

  protected Context<Receiver> defineContext()
  {
    // TODO remove dependency on Spring:
    // use a global dominion with a resource name.
    Context<Receiver> ctx = new ISpringContext<Receiver>(Receiver.class, file);
    return ctx;
  }

  protected File defineProjectDirectory() throws IOException
  {
    return new File(theDirectory + "/..").getCanonicalFile();
  }
}
