package net.objectof.model.impl.aggr;

import java.io.IOException;
import java.util.Iterator;

import net.objectof.aggr.Composite;
import net.objectof.aggr.Set;
import net.objectof.aggr.impl.IAggregate;
import net.objectof.aggr.impl.IArrayIterator;
import net.objectof.aggr.impl.IFieldset;
import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IId;

public class IComposite extends IAggregate<String, Object> implements
    Composite, Resource<Composite>
{
  private final IId<Composite> theId;
  private Object[] theData;

  public IComposite(IId<Composite> aId)
  {
    /*
     * TODO A new interface is created for each composite. Fix so a single
     * Aggregate Interface is used.
     */
    super(String.class, Object.class);
    theId = aId;
  }

  @Override
  public Object get(Object aKey)
  {
    return _(aKey.toString().intern());
  }

  @Override
  public String getUniqueName()
  {
    return theId.getUniqueName();
  }

  @Override
  public IId<Composite> id()
  {
    return theId;
  }

  @Override
  public boolean isAlterable()
  {
    return theId.lock() == null;
  }

  @Override
  public Iterator<Object> iterator()
  {
    return new IArrayIterator<>(getData());
  }

  @Override
  public Set<String> keySet()
  {
    return fields();
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
  public Object set(String aKey, Object aValue)
  {
    String key = aKey.intern();
    Object prior = _(key);
    _(key, aValue);
    return prior;
  }

  @Override
  public int size()
  {
    return getData().length;
  }

  @Override
  public String toString()
  {
    StringBuilder b = new StringBuilder();
    send("application/json", b);
    return b.toString();
  }

  @Override
  public Composite value() throws ResourceException
  {
    return this;
  }

  @Override
  public Object version() throws ResourceException
  {
    return theId.version();
  }

  /**
   * Optimized field retrieval.
   * 
   * @param aField
   *          an <b>interned</b> field name. (All constant strings are
   *          interned).
   * @return the Object at aField or null.
   */
  protected Object _(String aField)
  {
    return getData()[fields().internedNameIndex(aField)];
  }

  /**
   * Optimized field modifier.
   * 
   * @param aField
   *          an <b>interned</b> field name. (All constant strings are
   *          interned).
   * @param aValue
   *          The value to set.
   */
  protected void _(String aField, Object aValue)
  {
    getData()[fields().internedNameIndex(aField)] = aValue;
  }

  private final IFieldset fields()
  {
    return theId.kind().getFields();
  }

  private final Object[] getData()
  {
    if (theData == null)
    {
      theData = new Object[fields().size()];
      theId.load(this);
    }
    return theData;
  }

  @Override
  public Transaction tx() {
	return theId.tx();
  }
  
  
  @Override
  public boolean equals(Object o) {
	  if (!(o instanceof IComposite)) return false;
	  IComposite other = (IComposite) o;
	  return theId.equals(other.theId);
  }
  
}
