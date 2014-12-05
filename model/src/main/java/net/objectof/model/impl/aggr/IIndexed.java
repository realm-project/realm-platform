package net.objectof.model.impl.aggr;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.objectof.aggr.Listing;
import net.objectof.aggr.impl.IListing;
import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IId;

public class IIndexed<T> extends IListing<T> implements Resource<Listing<T>>
{
  private final IId<Listing<T>> theId;

  public IIndexed(IId<?> aId)
  {
    super(aId.kind().getParts().get(0).peer());
    @SuppressWarnings("unchecked")
    IId<Listing<T>> ident = (IId<Listing<T>>) aId;
    theId = ident;
    theId.load(this);
  }

  @Override
  public String getUniqueName()
  {
    return theId.getUniqueName();
  }

  @Override
  public IId<Listing<T>> id()
  {
    return theId;
  }

  @Override
  public void send(String aMediaType, Appendable aStream)
  {
    if (!aMediaType.equals("application/json"))
    {
      throw new IllegalArgumentException("Media type '" + aMediaType
          + "' not supported.");
    }
    try
    {
      theId.kind().datatype().toJson(this, theId.kind().getPackage(), aStream);
    }
    catch (IOException e)
    {
      throw new ResourceException(this, e);
    }
  }

  @Override
  public String toString()
  {
    Writer w = new StringWriter();
    try
    {
      theId.kind().datatype().toJson(this, theId.kind().getPackage(), w);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    return w.toString();
  }

  @Override
  public Listing<T> value() throws ResourceException
  {
    return this;
  }

  @Override
  public Object version() throws ResourceException
  {
    return theId.version();
  }

  @Override
  public Transaction tx() {
	return theId.tx();
  }
  
  @Override
  public boolean equals(Object o) {
	  if (!(o instanceof IIndexed)) return false;
	  IIndexed<?> other = (IIndexed<?>) o;
	  return theId.equals(other.theId);
  }
  
  
}
