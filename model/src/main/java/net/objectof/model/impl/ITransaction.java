package net.objectof.model.impl;

import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import net.objectof.Named;
import net.objectof.model.Id;
import net.objectof.model.Locator;
import net.objectof.model.Transaction;
import net.objectof.model.query.Query;
import net.objectof.model.query.parser.QueryBuilder;
import net.objectof.rt.impl.IFn;

public class ITransaction extends IFn implements Transaction, Locator
{
  private final IPackage thePackage;
//  private final Map<String, Object> theMap;
  public final Map<String, Object> theMap;
  private final Object theActor;
  private Status theStatus;
  protected int theLimit = 1000;

  public ITransaction(IPackage aPackage, Object aActor)
  {
    thePackage = aPackage;
    theActor = aActor;
    theMap = new ConcurrentHashMap<String, Object>();
    theStatus = Status.OPEN;
  }

  @Override
  public void close()
  {
    switch (theStatus)
    {
    case POSTING:
      throw new IllegalStateException();
    case OPEN:
    case POSTED:
    case FAILED:
      getMap().clear();
      theStatus = Status.CLOSED;
      break;
    case CLOSED:
      break;
    }
  }

  @Override
  public <T> T create(String aType)
  {
    if (theStatus != Status.OPEN)
    {
      throw new IllegalStateException();
    }
    @SuppressWarnings("unchecked")
    IKind<T> type = (IKind<T>) getPackage().forName(aType);
    if (type == null)
    {
      throw new IllegalArgumentException();
    }
    T inst = type.newInstance(this, type.nextTransientLabel());
    String key = inst instanceof Named ? ((Named) inst).getUniqueName() : inst
        .toString();
    getMap().put(key, inst);
    return inst;
  }

  public Object getActor()
  {
    return theActor;
  }

  @Override
  public IPackage getPackage()
  {
    return thePackage;
  }

  @Override
  public Status getStatus()
  {
    return theStatus;
  }

  public boolean isOpen()
  {
    return theStatus == Status.OPEN;
  }

  public void load(Object aValue)
  {
    if (theStatus != Status.OPEN)
    {
      throw new IllegalStateException();
    }
    // All values are expected to be transient, no loading required.
  }

  @Override
  public String locate(Id<?> aId)
  {
    return thePackage.locate(aId);
  }

  @Override
  public final synchronized void post()
  {
    if (theStatus != Status.OPEN)
    {
      throw new IllegalStateException();
    }
    theStatus = Status.POSTING;
    try
    {
      onPost();
    }
    catch (RuntimeException e)
    {
      theStatus = Status.FAILED;
      throw e;
    }
    theStatus = Status.POSTED;
  }

  public void put(String aName, Object aObject)
  {
    theMap.put(aName, aObject);
  }

  @Override
  public <T> T receive(String aMediaType, Reader aReader)
  {
    if (!aMediaType.equals("application/json"))
    {
      throw new RuntimeException("Unsupported media type.");
    }
    JsonReader reader = Json.createReader(aReader);
    JsonObject value = (JsonObject) reader.read();
    String loc = value.getString("loc");
    String kindName = kindFrom(loc);
    @SuppressWarnings("unchecked")
    IKind<T> kind = (IKind<T>) getPackage().forName(kindName);
    T val = kind.datatype().fromJson(this, value);
    return val;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T retrieve(String aType, Object aLabel)
  {
    if (theStatus != Status.OPEN)
    {
      throw new IllegalStateException();
    }
    if (aLabel == null)
    {
      return null;
    }
    return (T) theMap.get(getPackage().forName(aType).getUniqueName() + '/' + aLabel);
  }
  
	@Override
	public <T> T retrieve(Id<?> aId) {
		return retrieve(aId.kind().getComponentName(), aId.label().toString());
	}
	

  protected Map<String, Object> getMap()
  {
    return theMap;
  }

  protected void onPost()
  {
    throw new UnsupportedOperationException();
  }

  // TODO we need to rationalize & standard working with URI fragments.
  private String kindFrom(String aLoc)
  {
    // Start will be zero if no path...
    int start = aLoc.lastIndexOf('/') + 1;
    int end = aLoc.indexOf('-');
    if (end < 0)
    {
      end = aLoc.length();
    }
    return aLoc.substring(start, end);
  }
  
	@Override
	public <T> Iterable<T> enumerate(String kind) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Iterable<T> query(String kind, Query query) throws IllegalArgumentException,
			UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> Iterable<T> query(String kind, String query) {
		Query q = QueryBuilder.build(query, this);
		return query(kind, q);
	}

    
    @Override
    public void setLimit(int aLimit) {
    	theLimit = aLimit;
    }


}
