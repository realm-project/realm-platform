package net.objectof.model.impl;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.objectof.aggr.Aggregate;
import net.objectof.aggr.Set;
import net.objectof.model.Id;
import net.objectof.model.Kind;
import net.objectof.model.Resource;

public abstract class IResourceTx extends ITransaction
{
  protected static final int UNCHANGED = -1;
  protected static final int ARCHIVE = 0;
  protected static final int INSERT = 1;
  protected static final int UPDATE = 2;
  protected static final int DELETE = 3;

  public IResourceTx(IPackage aPackage, Object aActor)
  {
    super(aPackage, aActor);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Resource<Aggregate<Object, Object>> create(String aType)
  {
    if (getStatus() != Status.OPEN)
    {
      throw new IllegalStateException();
    }
    IKind<Resource<Aggregate<Object, Object>>> type = (IKind<Resource<Aggregate<Object, Object>>>) getPackage()
        .forName(aType);
    if (type == null)
    {
      throw new IllegalArgumentException();
    }
    Resource<Aggregate<Object, Object>> inst = type.newInstance(this,
        type.nextTransientLabel());
    getMap().put(inst.getUniqueName(), inst);
    return inst;
  }

  /**
   * This implementation simply creates the resource without verifying it
   * exists. The client must call value() or version() to ensure it is a valid
   * reference.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Resource<Aggregate<Object, Object>> retrieve(String aKind, Object aLabel)
  {
    Resource<Aggregate<Object, Object>> res = super.retrieve(aKind, aLabel);
    if (res == null)
    {
      Kind<?> kind = getPackage().forName(aKind);
      if (kind == null)
      {
        throw new IllegalArgumentException();
      }
      Object label = toLabel(aLabel);
      //TODO: 2014-08-29 - Add diagnostics
      res = create(kind, label);
      getMap().put(res.getUniqueName(), res);
    }
    return res;
  }

  protected Resource<Aggregate<Object, Object>> create(Kind<?> aKind,
      Object aLabel)
  {
    @SuppressWarnings("unchecked")
    IKind<Resource<Aggregate<Object, Object>>> kind = (IKind<Resource<Aggregate<Object, Object>>>) aKind;
    Resource<Aggregate<Object, Object>> instance = kind.newInstance(this, aLabel);
    return instance;
//    return kind.newInstance(this, aLabel);
  }

  protected int onElement(Id<?> aInstance, Object aKey, Object aCurrent,
      Object aNew)
  {
    if (aNew != null && aCurrent == null)
    {
      onElementInsert(aInstance, aKey, aNew);
      return INSERT;
    }
    if (aNew != null && !aNew.equals(aCurrent))
    {
      onElementUpdate(aInstance, aKey, aCurrent, aNew);
      return UPDATE;
    }
    if (aNew == null && aCurrent != null)
    {
      onElementDelete(aInstance, aKey, aCurrent);
      return DELETE;
    }
    return UNCHANGED;
  }

  protected abstract void onElementDelete(Id<?> aInstance, Object aIndex,
      Object aCurrent);

  protected abstract void onElementInsert(Id<?> aInstance, Object aIndex,
      Object aNew);

  protected abstract void onElementUpdate(Id<?> aInstance, Object aKey,
      Object aCurrent, Object aNew);

  protected abstract void onEndPost();

  @SuppressWarnings("unchecked")
  @Override
  protected void onPost()
  {
	  onStartPost();
	  
	  Collection<Object> values = getMap().values();
	  Collection<Object> valuesCopy = new ArrayList<Object>(values);
	  
//  	  for (Object o : getMap().values())
	  for (Object o : valuesCopy)
  	  {  
  		  onPostItem((Resource<Aggregate<Object, Object>>)o);
  	  }
  	  onEndPost();
  }

  protected boolean onPostItem(Resource<Aggregate<Object, Object>> aInstance)
  {
	  Map<String, Object> theMapBackup = new ConcurrentHashMap<String, Object>();
	  theMapBackup.putAll(super.theMap);
	  
    Id<?> ident = aInstance.id();
    Aggregate<Object, Object> edited = aInstance.value();
    Aggregate<Object, Object> current = create(ident.kind(), ident.label())
        .value();

//    For debug purposes
//    Kind<?> k = ident.kind();
//    Object l = ident.label();
//    Resource<Aggregate<Object, Object>> resource = create(k, l);
//    Aggregate<Object, Object> current = resource.value();

    boolean hasInserts = false;
    Set<Object> keySet = null;
    
//    Changes made to resolve the remove-the-last-item-of-the-list bug
    if (edited.keySet().size() >= current.keySet().size())
    	keySet = edited.keySet();
    else // edited.keySet().size() < current.keySet().size()
    	keySet = current.keySet();
    
//    for (Object key : edited.keySet())
    for (Object key : keySet)
    {
    	Object editValue = edited.get(key);
    	Object currentValue = current.get(key);
    	if (onElement(ident, key, currentValue, editValue) == INSERT)
    	{
    		hasInserts = true;
    	}

    	super.theMap.clear();
    	super.theMap.putAll(theMapBackup);
    }
    
    return hasInserts;
  }

  protected abstract void onPrepare(
      Resource<Aggregate<Object, Object>> aResource);

  @SuppressWarnings("unchecked")
  protected void onStartPost()
  {
    for (Object o : getMap().values())
    {
      onPrepare((Resource<Aggregate<Object, Object>>)o);
    }
  }

  protected Object toLabel(Object aId)
  {
    return aId;
  }
}
