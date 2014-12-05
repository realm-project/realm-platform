package net.objectof.model.impl.facets;

import java.io.IOException;

import net.objectof.model.Id;
import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.impl.IBaseId;
import net.objectof.model.impl.IId;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.ITransaction;

public class IResource<T> extends IId<T> implements Resource<T>
{
  private final IId<T> theId;
  private T theValue;

  public IResource(IId<T> aFrom)
  {
    theId = aFrom;
  }

  public IResource(IId<T> aId, T aValue)
  {
    this(aId);
    theValue = aValue;
  }

  public IResource(IKind<T> aKind)
  {
    this(new IBaseId<>(null, aKind, aKind.nextTransientLabel()));
  }

  @Override
  public boolean equals(Object aObject)
  {
    if (aObject instanceof IResource)
    {
      IResource<?> res = (IResource<?>) aObject;
      return res.kind() == kind() && res.from().label() == theId.label();
    }
    return false;
  }

  @Override
  public IId<T> from()
  {
    return theId;
  }

  @Override
  public String getUniqueName()
  {
    return theId.getUniqueName();
  }

  @Override
  public int hashCode()
  {
    return getUniqueName().hashCode();
  }

  @Override
  public Id<T> id()
  {
    return theId;
  }

  @Override
  public IKind<T> kind()
  {
    return theId.kind();
  }

  @Override
  public Object label()
  {
    return theId.label();
  }

  @Override
  public void relabel(Object aPermanentLabel)
  {
    theId.relabel(aPermanentLabel);
  }

  @Override
  public void send(String aMediaType, Appendable aStream)
  {
    if (aMediaType.equals("application/json"))
    {
      throw new IllegalArgumentException("Media type '" + aMediaType
          + "' not supported.");
    }
    try
    {
      theId.kind().datatype()
      .toJson(this.value(), theId.kind().getPackage(), aStream);
    }
    catch (IOException e)
    {
      throw new ResourceException(this, e);
    }
  }

  @Override
  public String toString()
  {
    return theId.toString();
  }

  @Override
  public ITransaction tx()
  {
    return theId.tx();
  }

  @Override
  public T value() throws ResourceException
  {
    T value = theValue;
    return value == null ? load() : value;
  }

  @Override
  public Object version() throws ResourceException
  {
    return theId.version();
  }

  protected synchronized T load()
  {
    T value = theValue;
    // Check again, while synchronized, to handle race conditions.
    if (value == null)
    {
      value = kind().newInstance(theId.tx(), label());
      theValue = value;
    }
    return value;
  }
}
