package net.objectof.aggr.impl;

public class IArrayView<E> extends IArray<E>
{
  public final boolean isImmutable;

  @SafeVarargs
  public IArrayView(boolean aCloneIndicator, E... aArray)
  {
    super(aCloneIndicator ? aArray.clone() : aArray);
    isImmutable = aCloneIndicator;
  }

  @Override
  public final boolean isAlterable()
  {
    return false;
  }
}