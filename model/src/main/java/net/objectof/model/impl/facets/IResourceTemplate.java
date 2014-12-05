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
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public class IResourceTemplate extends ITemplate<IKind<?>> implements
    Facet<IKind<?>>
{
  public IResourceTemplate(ITemplateContext aContext)
  {
    super(aContext, "resource.vm");
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
    String implementation = getSuperClass(aResource);
    context.put("implementation", implementation);
    String implementationKind = getImplKind(aResource);
    context.put("implementationKind", implementationKind);
    context.put("ident", IId.class.getName() + '<' + Composite.class.getName()
        + '>');
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    String packagePath = aKind.getPackage().getComponentName()
        .replace('.', '/');
    packagePath += "/impl";
    File dir = new File(getContext().get("/").toString() + '/' + packagePath);
    dir.mkdirs();
    String fileName = 'I'
        + Util.initCap(Util.selector(aKind.getComponentName())) + ".java";
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

  protected String getSuperClass(IKind<?> aResource)
  {
    Object base = aResource
        .getProperty("objectof.net:1401/model/implementation");
    if (base != null)
    {
      return base.toString();
    }
    return IResource.class.getName() + '<' + Composite.class.getName() + '>';
  }
}
