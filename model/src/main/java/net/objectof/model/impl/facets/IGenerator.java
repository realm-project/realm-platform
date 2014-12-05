package net.objectof.model.impl.facets;

import java.io.File;
import java.io.IOException;

import net.objectof.Selector;
import net.objectof.aggr.Aggregate;
import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.model.Package;
import net.objectof.model.impl.IMetamodel;
import net.objectof.rt.impl.IFn;

import org.w3c.dom.Document;

/**
 * An IGenerator provides the framework for model generation via templates.
 *
 * @author jdh
 *
 */
@Selector
public class IGenerator extends IFn
{
  private final Facet<Annotated> theFacet;
  private final String theSourcePath;
  private final String theOutputPath;
  private final IMetamodel theMetamodel;
  private File theRoot;
  private final Aggregate<String, Object> theContext;

  public IGenerator(String aRootPath, String aSourcePath, String aOutputPath,
      IMetamodel aMetamodel, Aggregate<String, Object> aContext,
      Facet<Annotated> aFacet)
  {
    // setRoot(new File(aRootPath));
    theSourcePath = aSourcePath;
    theMetamodel = aMetamodel;
    theFacet = aFacet;
    theOutputPath = aOutputPath;
    theContext = aContext;
  }

  public File getOutputDirectory() throws IOException
  {
    File f = new File(theRoot, theOutputPath);
    System.out.println(f.getCanonicalPath());
    return f.exists() && f.isDirectory() ? f : null;
  }

  public File getSourceDirectory() throws IOException
  {
    File f = new File(theRoot, theSourcePath);
    System.out.println(f.getCanonicalPath());
    return f.exists() && f.isDirectory() ? f : null;
  }

  /**
   * This method enables a Maven Mojo to process this. The selector must be
   * named "process" as the Mojo only requires a Receiver interface.
   *
   * @param aFile
   */
  @Selector
  public void process() throws Exception
  {
    File pkgs = getSourceDirectory();
    if (pkgs == null)
    {
      return;
    }
    for (File pkgFile : pkgs.listFiles())
    {
      Package pkg = loadPackage(pkgFile);
      if (pkg != null)
      {
        for (Annotated kind : pkg.getParts())
        {
          theFacet.process(kind);
        }
      }
    }
  }

  /**
   * This method enables a Maven Mojo to define a project's root. The selector
   * must be named "root:" as the Mojo only requires a Receiver interface.
   *
   * @param aFile
   */
  @Selector("root:")
  public void setRoot(File aFile) throws IOException
  {
    theRoot = aFile;
    theContext.set("/", getOutputDirectory());
  }

  protected ISourcePackage createPackage(Document doc)
  {
    // TODO implement a configurable factory here...
    return new ISourcePackage(getMetamodel(), doc);
  }

  protected IMetamodel getMetamodel()
  {
    return theMetamodel;
  }

  protected Package loadPackage(File aSchemaFile) throws IOException
  {
    Document doc = ISourcePackage.parseFile(aSchemaFile);
    return aSchemaFile.getName().endsWith(".xml") ? createPackage(doc) : null;
  }
}
