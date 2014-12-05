package net.objectof.aggr.impl;

import net.objectof.aggr.Aggregate;
import net.objectof.ext.Vector;
import net.objectof.rt.Instance;
import net.objectof.rt.Interface;
import net.objectof.rt.Member;
import net.objectof.rt.Runtime;
import net.objectof.rt.impl.IInterface;

public class IAggregateInterface extends IInterface
{
  public static IInterface newInstance(IInterface aInterface,
      Class<?> aKeyType, Class<?> aValueType)
  {
    return new IAggregateInterface(aInterface, new IAggregateKeyParameter(
        aInterface.runtime().forClass(aKeyType)), aInterface.runtime()
        .forClass(aValueType));
  }

  public static IInterface newInstance(IInterface aInterface,
      Interface aKeyType, Interface aValueType)
  {
    return new IAggregateInterface(aInterface, new IAggregateKeyParameter(
        aKeyType), aValueType);
  }

  private final IInterface theInterface;
  private final Interface theValueType;
  private final Vector<Interface> theKeyType;

  public IAggregateInterface(IInterface aInterface,
      Vector<Interface> aParameterTypes, Interface aValueType)
  {
    theInterface = aInterface;
    if (aParameterTypes == null)
    {
      throw new RuntimeException();
    }
    theKeyType = aParameterTypes;
    theValueType = aValueType;
  }

  @Override
  public Instance create(String aSelector, Object aReceiver)
  {
    return theInterface.create(aSelector, aReceiver);
  }

  @Override
  public Object dispatch(String aSelector, Object aReceiver, Object... aMessage)
  {
    return theInterface.dispatch(aSelector, aReceiver, aMessage);
  }

  @Override
  public Interface getEvaluation()
  {
    return theValueType;
  }

  @Override
  public Aggregate<String, Member> getMembers()
  {
    @SuppressWarnings("unchecked")
    Aggregate<String, Member> members = (Aggregate<String, Member>) theInterface
    .getMembers();
    return members;
  }

  @Override
  public Vector<? extends Interface> getParameters()
  {
    return theKeyType;
  }

  @Override
  public Class<?> peer()
  {
    return theInterface.peer();
  }

  @Override
  public Runtime<? extends IInterface> runtime()
  {
    return theInterface.runtime();
  }
}
