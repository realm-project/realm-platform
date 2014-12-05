package net.objectof.rt.impl;

import net.objectof.Context;
import net.objectof.InvalidNameException;
import net.objectof.Type;
import net.objectof.aggr.Aggregate;
import net.objectof.ext.Vector;
//import net.objectof.aggr.impl.IArray;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Member;

/**
 * A Peer is a simple type providing a relationship to the host type. In Java, a
 * Class is the peer.
 *
 * @author jdh
 *
 */
public abstract class IInterface extends IType implements Interface,
    Context<Member>
{
  public static void buildSignature(StringBuilder aBuilder, Interface aInterface)
  {
    if (aInterface == null)
    {
      return;
    }
    if (aInterface.getEvaluation() != null)
    {
      buildSignature(aBuilder, aInterface.getEvaluation());
      aBuilder.append(' ');
    }
    String un = aInterface.getUniqueName();
    aBuilder.append(un.substring(un.lastIndexOf('.') + 1));
    Vector<? extends Interface> parameters = aInterface.getParameters();
    if (parameters == null)
    {
      return;
    }
    aBuilder.append('(');
    String delim = "";
    int len = parameters.size();
    for (int i = 0; i < len; i++)
    {
      buildSignature(aBuilder, parameters.get(i));
      aBuilder.append(delim);
      delim = ", ";
    }
    aBuilder.append(')');
  }

  public abstract Instance create(String aSelector, Object aReceiver);

  public abstract Object dispatch(String aSelector, Object aReceiver,
      Object... aMessage);

  @Override
  public Member forName(String aSelector) throws InvalidNameException
  {
    Member member = getMembers().get(aSelector);
    if (member == null)
    {
      throw new InvalidNameException(aSelector);
    }
    return member;
  }

  @Override
  public Interface getEvaluation()
  {
    return null;
  }

  @Override
  public abstract Aggregate<String, ? extends Member> getMembers();

  @Override
  public Vector<? extends Interface> getParameters()
  {
    return null;
  }

  public String getPathname()
  {
    return peer().getName();
  }

  public String getSelector()
  {
    String name = getPathname();
    return name.substring(name.lastIndexOf('.') + 1);
  }

  public String getSignature()
  {
    StringBuilder b = new StringBuilder();
    buildSignature(b, this);
    return b.toString();
  }

  @Override
  public String getUniqueName()
  {
    return runtime().getUniqueName() + '/' + getPathname();
  }

  @Override
  public boolean isAssignable(Type aType)
  {
    return aType instanceof IInterface
        && peer().isAssignableFrom(((IInterface) aType).peer());
  }
}
