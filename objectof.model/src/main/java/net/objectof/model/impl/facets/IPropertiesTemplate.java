package net.objectof.model.impl.facets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.objectof.facet.Facet;
import net.objectof.model.impl.IKind;
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public class IPropertiesTemplate extends ITemplate<IKind<?>> implements
    Facet<IKind<?>>
{
  public IPropertiesTemplate(ITemplateContext aContext)
  {
    super(aContext, "properties.vm");
  }

  @Override
  public Void process(IKind<?> aKind) throws Exception
  {
    // Properties are generated based on whole entities.
    if (aKind.getPartOf() == null)
    {
      super.process(aKind);
    }
    return null;
  }

  @Override
  protected Context defineContext(IKind<?> aResource)
  {
    Context context = super.defineContext(aResource);
    context.put("util", Util.class);
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    String root = getContext().get("/").toString();
    File dir = new File(root + "/src/test/output");
    dir.mkdirs();
    String fileName = aKind.getComponentName() + ".json";
    File f = new File(dir, fileName);
    return new FileWriter(f);
  }
}
