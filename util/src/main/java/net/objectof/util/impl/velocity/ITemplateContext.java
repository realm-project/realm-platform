package net.objectof.util.impl.velocity;

import net.objectof.Context;
import net.objectof.rt.impl.base.IUtilContext;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class ITemplateContext extends IUtilContext<Object> implements
    org.apache.velocity.context.Context
{
  private final VelocityEngine theEngine;

  public ITemplateContext(Context<Object> aParent, String aName)
  {
    super(aParent, aName);
    theEngine = new VelocityEngine();
    theEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    theEngine.setProperty("classpath.resource.loader.class",
        ClasspathResourceLoader.class.getName());
    theEngine.init();
  }

  public ITemplateContext(String aName)
  {
    this(null, aName);
  }

  @Override
  public boolean containsKey(Object aKey)
  {
    return getMap().containsKey(aKey);
  }

  @Override
  public Object get(String aKey)
  {
    return forName(aKey);
  }

  public VelocityEngine getEngine()
  {
    return theEngine;
  }

  @Override
  public Object[] getKeys()
  {
    return getMap().keySet().toArray();
  }

  @Override
  public Object put(String aKey, Object aValue)
  {
    return set(aKey, aValue);
  }

  @Override
  public Object remove(Object aKey)
  {
    return getMap().remove(aKey);
  }
}
