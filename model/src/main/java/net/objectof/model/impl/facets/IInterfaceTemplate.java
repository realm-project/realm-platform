package net.objectof.model.impl.facets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.objectof.Selector;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public class IInterfaceTemplate extends ITemplate<IKind<?>>
{
  public IInterfaceTemplate(ITemplateContext aContext)
  {
    super(aContext, "interface.vm");
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
    return context;
  }

  @Override
  protected Writer defineWriter(IKind<?> aKind) throws IOException
  {
    String packagePath = aKind.getPackage().getComponentName()
        .replace('.', '/');
    File dir = new File(getContext().get("/").toString() + '/' + packagePath);
    dir.mkdirs();
    String fileName = Util.initCap(Util.selector(aKind.getComponentName()))
        + ".java";
    File f = new File(dir, fileName);
    return new FileWriter(f);
  }
}
