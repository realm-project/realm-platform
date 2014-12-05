package net.objectof.model.impl.facets;

import java.io.IOException;
import java.io.Writer;

import net.objectof.facet.Annotated;
import net.objectof.model.Package;
import net.objectof.util.Streamer;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

public class IPackageTemplate extends ITemplate<Package>
{
  private final Streamer<Annotated> theTemplate;

  public IPackageTemplate(ITemplateContext aContext,
      Streamer<Annotated> aTemplate)
  {
    super(aContext, null);
    theTemplate = aTemplate;
  }

  @Override
  public void stream(Writer aWriter, Package aPackage) throws IOException
  {
    Writer w = defineWriter(aPackage);
    for (Annotated kind : aPackage.getParts())
    {
      streamKind(kind, w);
    }
  }

  /**
   * Override to suppress processing of one or more Kinds.
   */
  protected boolean isProcessed(Annotated aKind)
  {
    return true;
  }

  protected void streamKind(Annotated aKind, Writer aWriter) throws IOException
  {
    if (isProcessed(aKind))
    {
      theTemplate.stream(aWriter, aKind);
    }
  }
}
