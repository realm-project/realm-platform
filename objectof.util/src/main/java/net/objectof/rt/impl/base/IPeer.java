package net.objectof.rt.impl.base;

import net.objectof.InvalidNameException;
import net.objectof.rt.Instance;

/**
 * A Peer is a simple type providing a relationship to the host type. In Java, a
 * Class is the peer.
 *
 * @author jdh
 *
 */
public class IPeer<T> extends ILoadableInterface
{
  public IPeer(Class<? extends T> aClass)
  {
    super(aClass);
  }

  @Override
  public Instance create(String aSelector, Object aReceiver)
  {
    if (aReceiver.getClass() != peer())
    {
      throw new IllegalArgumentException();
    }
    return new IFunction(aReceiver, forName(aSelector));
  }

  @Override
  public Object dispatch(String aSelector, Object aReceiver, Object... aMessage)
  {
    return forName(aSelector).invoke(aReceiver, aMessage);
  }

  @Override
  public IMethod forName(String aSelector) throws InvalidNameException
  {
    return (IMethod) super.forName(aSelector);
  }

  @Override
  public Class<? extends T> peer()
  {
    /*
     * This is a safe cast. See the constructor.
     */
    @SuppressWarnings("unchecked")
    Class<? extends T> ret = (Class<? extends T>) super.peer();
    return ret;
  }

  @Override
  protected ILoader getLoader()
  {
    return IMethodLoader.INSTANCE;
  }
}
