package net.objectof.util.impl.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.objectof.facet.Annotated;
import net.objectof.facet.Facet;
import net.objectof.facet.Property;
import net.objectof.util.Streamer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

public class ITemplate<T extends Annotated> implements Streamer<T>, Facet<T>
{
  private final ITemplateContext theContext;
  private final String theTemplateName;

  public ITemplate(ITemplateContext aContext, String aTemplateName)
  {
    theContext = aContext;
    theTemplateName = aTemplateName;
  }

  public String getName()
  {
    return theTemplateName;
  }

  @Override
  public String getUniqueName()
  {
    return theContext.getUniqueName() + '/' + theTemplateName;
  }

  @Override
  public Property process(Annotated aObject, String aSelector)
  {
    return aObject.getProperty(getUniqueName() + '/' + aSelector);
  }

  @Override
  public Object process(T aObject) throws Exception
  {
    Writer w = defineWriter(aObject);
    try
    {
      stream(w, aObject);
    }
    finally
    {
      w.close();
    }
    return w.toString();
  }

  @Override
  public void stream(Writer aWriter, T aObject) throws IOException
  {
    VelocityEngine engine = theContext.getEngine();
    Context ctx = defineContext(aObject);
    Template t = engine.getTemplate(theTemplateName);
    t.merge(ctx, aWriter);
  }

  /**
   * This implementation creates a new Context from the ITemplates and puts the
   * Faceted Resource into it as "a" (argument). If you know there will be no
   * threading issues, you could return the common shared templates context.
   * 
   * @param aResource
   * @return
   */
  protected Context defineContext(T aObject)
  {
    Context ctx = new VelocityContext(theContext);
    ctx.put("a", aObject);
    return ctx;
  }

  protected Writer defineWriter(T aObject) throws IOException
  {
    return new StringWriter();
  }

  protected final ITemplateContext getContext()
  {
    return theContext;
  }
}
