package net.objectof.model.impl.aggr;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.objectof.aggr.Mapping;
import net.objectof.aggr.impl.IMapping;
import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IId;

public class IMapped<T> extends IMapping<String, T> implements
    Resource<Mapping<String, T>>
{
  private final IId<Mapping<String, T>> theId;

  public IMapped(IId<?> aId)
  {
//    super(String.class, aId.kind().getMembers().get(0).peer());
    super(String.class, aId.kind().getParts().get(0).peer());
    @SuppressWarnings("unchecked")
    IId<Mapping<String, T>> ident = (IId<Mapping<String, T>>) aId;
    theId = ident;
    theId.load(this);
  }

  @Override
  public String getUniqueName()
  {
    return theId.getUniqueName();
  }

  @Override
  public IId<Mapping<String, T>> id()
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
  public Mapping<String, T> value() throws ResourceException
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
	  if (!(o instanceof IMapped)) return false;
	  IMapped<?> other = (IMapped<?>) o;
	  return theId.equals(other.theId);
  }
  
}
