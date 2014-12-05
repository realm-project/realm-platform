package net.objectof.rt.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.objectof.Context;

/**
 * Provides a mapping between a type name and a Type. The public methods are
 * thread-safe to ensure a type is loaded once.
 * 
 * @author jdh
 * 
 * @param <T>
 */
public class IContext<T> extends IFn implements Context<T>, Iterable<T>
{
  public static final String ANS_SCHEME = "ans://";
  private Context<T> theParentContext;
  private String theName;
  private final Map<String, T> theMap;

  public IContext(Context<T> aParentContext, String aName)
  {
    this(aParentContext, aName, new HashMap<String, T>());
  }

  public IContext(Context<T> aParentContext, String aName, Map<String, T> aMap)
  {
    theParentContext = aParentContext;
    theName = aName;
    theMap = aMap;
  }

  public IContext(String aName)
  {
    this(aName, new HashMap<String, T>());
  }

  public IContext(String aName, Map<String, T> aMap)
  {
    this(null, aName, aMap);
  }

  @Override
  public T forName(String aName)
  {
    T value = theMap.get(aName);
    return value == null && theParentContext != null ? theParentContext
        .forName(aName) : value;
  }

  @Override
  public String getUniqueName()
  {
    return theParentContext == null ? theName : theParentContext
        .getUniqueName() + '/' + theName;
  }

  @Override
  public Iterator<T> iterator()
  {
    return theMap.values().iterator();
  }

  @Override
  public String toString()
  {
    return ANS_SCHEME + getUniqueName();
  }

  protected char getDelimiter()
  {
    return '.';
  }

  protected Map<String, T> getMap()
  {
    return theMap;
  }

  protected String getName()
  {
    return theName;
  }
}
