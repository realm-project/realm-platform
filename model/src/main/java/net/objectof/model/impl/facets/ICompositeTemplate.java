package net.objectof.model.impl.facets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.objectof.Selector;
import net.objectof.aggr.Composite;
import net.objectof.facet.Facet;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IId;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.aggr.IComposite;
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public class ICompositeTemplate extends ITemplate<IKind<?>> implements
    Facet<IKind<?>>
{
  public ICompositeTemplate(ITemplateContext aContext)
  {
    super(aContext, "composite.vm");
  }

  @Override
  public Void process(IKind<?> aKind) throws Exception
  {
    if (aKind.getStereotype() == Stereotype.COMPOSED)
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
    context.put("implementation", IComposite.class.getName());
    context.put("type", IId.class.getName());
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    String packagePath = aKind.getPackage().getComponentName()
        .replace('.', '/');
    packagePath += "/composite";
    File dir = new File(getContext().get("/").toString() + '/' + packagePath);
    dir.mkdirs();
    String fileName = 'I'
        + Util.initCap(Util.selector(aKind.getComponentName())) + "Bean.java";
    File f = new File(dir, fileName);
    return new FileWriter(f);
  }

  protected String getImplKind(IKind<?> aResource)
  {
    Object kind = aResource
        .getProperty("objectof.net:1401/model/implementationKind");
    if (kind != null)
    {
      return kind.toString();
    }
    return IKind.class.getName() + '<' + Composite.class.getName() + '>';
  }

  protected String getSuperClass(IKind<?> aKind)
  {
    Object base = aKind.getProperty("objectof.net:1401/model/implementation");
    if (base != null)
    {
      return base.toString();
    }
    return IComposite.class.getName();
  }
}
