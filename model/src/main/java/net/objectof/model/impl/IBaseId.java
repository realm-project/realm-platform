package net.objectof.model.impl;

public class IBaseId<T> extends IId<T>
{
  private final ITransaction theTx;
  private final IKind<T> theKind;
  private Object theLabel;

  public IBaseId(ITransaction aTx, IKind<T> aType)
  {
    theTx = aTx;
    theKind = aType;
    theLabel = aType.nextTransientLabel();
  }

  public IBaseId(ITransaction aTx, IKind<T> aType, Object aLabel)
  {
    theTx = aTx;
    theLabel = aLabel;
    theKind = aType;
  }

  @Override
  public IKind<T> kind()
  {
    return theKind;
  }

  @Override
  public Object label()
  {
    return theLabel;
  }

  @Override
  public void load(T aValue)
  {
    theTx.load(aValue);
  }

  @Override
  public void relabel(Object aPermanentLabel)
  {
    theLabel = aPermanentLabel;
  }

  @Override
  public ITransaction tx()
  {
    return theTx;
  }

  @Override
  public Object version()
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }
}
