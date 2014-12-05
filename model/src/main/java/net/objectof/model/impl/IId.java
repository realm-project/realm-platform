package net.objectof.model.impl;

import net.objectof.model.Id;
import net.objectof.rt.impl.IFn;

public abstract class IId<T> extends IFn implements Id<T>
{
  @Override
  public boolean equals(Object aObject)
  {
    return aObject instanceof Id && equalsId((Id<?>) aObject);
  }

  @Override
  public String getUniqueName()
  {
    return kind().getUniqueName() + '/' + label();
  }

  @Override
  public int hashCode()
  {
    int hash = 23;
    hash = hash * 37 + kind().hashCode();
    hash = hash * 37 + label().hashCode();
    return hash;
  }

  @Override
  public abstract IKind<T> kind();

  @Override
  public abstract Object label();

  public void load(T aValue)
  {
  }

  public Object lock()
  {
    return null;
  }

  public abstract void relabel(Object aPermanentLabel);

  @Override
  public String toString()
  {
    return "ans://" + getUniqueName();
  }

  public abstract ITransaction tx();

  public abstract Object version();

  protected IId<T> from()
  {
    return this;
  }

  private final boolean equalsId(Id<?> aId)
  {
    return kind().equals(aId.kind()) && label().equals(aId.label());
  }
}
