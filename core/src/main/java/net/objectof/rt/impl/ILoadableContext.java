package net.objectof.rt.impl;



/**
 * Provides a mapping between a type name and a Type. The public methods are
 * thread-safe to ensure a type is loaded once.
 * 
 * @author jdh
 * 
 * @param <T>
 */
public abstract class ILoadableContext<T> extends IContext<T>
{
  public ILoadableContext(String aName)
  {
    super(aName);
  }

  @Override
  public final T forName(String aName)
  {
    T object = super.forName(aName);
    if (object == null && !getMap().containsKey(aName))
    {
      object = get(aName);
    }
    return object;
  }

  protected abstract T load(String aName);

  private synchronized final T get(String aName)
  {
    // Again, while synchronized to handle race conditions:
    T object = super.forName(aName);
    if (object == null)
    {
      object = load(aName);
      getMap().put(aName, object);
    }
    return object;
  }
}
