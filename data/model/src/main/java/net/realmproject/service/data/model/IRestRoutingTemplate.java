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

public class IRestRoutingTemplate extends ITemplate<IKind<?>> implements
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

  public IRestRoutingTemplate(ITemplateContext aContext)
  {
    super(aContext, "rest-routing.vm");
  }

  @Override
  public Void process(IKind<?> aKind) throws Exception
  {
    if (hasRestProperties(aKind))
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
    context.put("selector", Selector.class.getName());
    context.put("resttemplate", IRestRoutingTemplate.class);
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    String packagePath = aKind.getPackage().getComponentName()
        .replace('.', '/');
    File dir = new File(getContext().get("/").toString() + '/' + packagePath
        + "/" + "routers");
    dir.mkdirs();
    String fileName = Util.initCap(Util.selector(aKind.getComponentName()))
        + ".xml";
    File f = new File(dir, fileName);
    return new FileWriter(f);
  }
}
