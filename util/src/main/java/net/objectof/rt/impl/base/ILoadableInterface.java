package net.objectof.rt.impl.base;

import java.lang.reflect.Method;

import net.objectof.Selector;
import net.objectof.aggr.Aggregate;
import net.objectof.aggr.Mapping;
import net.objectof.aggr.impl.IMapping;
import net.objectof.rt.Member;
import net.objectof.rt.impl.IInterface;

/**
 * A Peer is a simple type providing a relationship to the host type. In Java, a
 * Class is the peer.
 *
 * @author jdh
 *
 */
public abstract class ILoadableInterface extends IInterface
{
  private final Class<?> theClass;
  private Mapping<String, Member> theMembers;

  public ILoadableInterface(Class<?> aClass)
  {
    theClass = aClass;
  }

  @Override
  public Aggregate<String, Member> getMembers()
  {
    Mapping<String, Member> members = theMembers;
    if (members == null)
    {
      synchronized (this)
      {
        members = theMembers;
        // Check the state again while synchronized to handle race conditions:
        if (members == null)
        {
          ILoader loader = getLoader();
          members = create();
          loader.load(this, members);
        }
      }
      theMembers = members;
    }
    return members;
  }

  public boolean isAnnotated()
  {
    return theClass.getAnnotation(Selector.class) != null;
  }

  @Override
  public Class<?> peer()
  {
    Adaptor adaptor = theClass.getAnnotation(Adaptor.class);
    return adaptor == null ? theClass : adaptor.value();
  }

  protected abstract ILoader getLoader();

  protected Method[] getMethods()
  {
    return theClass.getDeclaredMethods();
  }

  private Mapping<String, Member> create()
  {
    Mapping<String, Member> mapping = new IMapping<String, Member>(
        String.class, Member.class);
    return mapping;
  }
}
