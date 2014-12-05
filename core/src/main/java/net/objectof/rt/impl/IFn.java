package net.objectof.rt.impl;

import net.objectof.ext.Dominion;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Runtime;

/**
 * Supports Typed via simple peering to this object's Class.
 *
 * @author jdh
 *
 */
public class IFn implements Instance
{
  public static final Object[] NO_ARGS = new Object[0];
  public static final String DEFAULT_RUNTIME = "rt.objectof.net:1401/java";

  public static final Runtime<? extends IInterface> defaultRuntime()
  {
    return getNamed(DEFAULT_RUNTIME);
  }

  public static <T> T getNamed(String aUniqueName)
  {
    @SuppressWarnings("unchecked")
    T object = (T) Dominion.INSTANCE.find(aUniqueName);
    return object;
  }

  @Override
  public Instance apply(Object... aMessage)
  {
    if (aMessage.length == 0)
    {
      return this;
    }
    throw new IllegalArgumentException("Extraneous arguments to apply().");
  }

  @Override
  public Object evaluate(Object... aMessage)
  {
    if (aMessage.length == 0)
    {
      return this;
    }
    throw new IllegalArgumentException("Extraneous arguments to evaluate().");
  }

  public final Object perform(String aSelector)
  {
    return perform(aSelector, NO_ARGS);
  }

  @Override
  public Object perform(String aSelector, Object... aMessage)
  {
    return iinterface().dispatch(aSelector, this, aMessage);
  }

  /**
   * Override to provide different type implementations.
   */
  public Runtime<? extends IInterface> runtime()
  {
    return defaultRuntime();
  }

  @Override
  public Instance select(String aSelector)
  {
    return iinterface().create(aSelector, this);
  }

  @Override
  public Interface type()
  {
    return iinterface();
  }

  private final IInterface iinterface()
  {
    return runtime().forClass(getClass());
  }
}
