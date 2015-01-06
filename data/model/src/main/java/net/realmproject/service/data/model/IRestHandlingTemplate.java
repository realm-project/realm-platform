package net.realmproject.service.data.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.objectof.Selector;
import net.objectof.facet.Facet;
import net.objectof.model.impl.IKind;
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public class IRestHandlingTemplate extends ITemplate<IKind<?>> implements
    Facet<IKind<?>>
{
  public static boolean hasRestProperties(IKind<?> kind)
  {
    boolean rest = false;
    for (String property : kind.getPropertyNames())
    {
      rest |= property.startsWith("ans://realmproject.net:1401/rest/model");
    }
    return rest;
  }

  public IRestHandlingTemplate(ITemplateContext aContext)
  {
    super(aContext, "rest-handling.vm");
  }

  @Override
  public Void process(IKind<?> aKind) throws Exception
  {
    if (!hasRestProperties(aKind))
    {
      return null;
    }
    if (getTargetFile(aKind).exists())
    {
      return null;
    }
    super.process(aKind);
    return null;
  }

  @Override
  protected Context defineContext(IKind<?> aResource)
  {
    Context context = super.defineContext(aResource);
    context.put("util", Util.class);
    context.put("selector", Selector.class.getName());
    context.put("resttemplate", IRestHandlingTemplate.class);
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    return new FileWriter(getTargetFile(aKind));
  }

  private File getTargetFile(IKind<?> kind)
  {
    String packagePath = kind.getPackage().getComponentName().replace('.', '/');
    File dir = new File(getContext().get("/").toString() + '/' + packagePath
        + "/" + "handlers");
    dir.mkdirs();
    String fileName = "I"
        + Util.initCap(Util.selector(kind.getComponentName())) + "Handler.java";
    return new File(dir, fileName);
  }
}
