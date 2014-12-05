package net.objectof.aggr.impl;

import java.util.Iterator;

import net.objectof.aggr.Set;

public class IFieldset extends IAggregate<String, String> implements
    Set<String>
{
  private final String[] theValues;

  public IFieldset(String... aStrings)
  {
    super(String.class, String.class);
    int len = aStrings.length;
    theValues = new String[len];
    for (int i = 0; i < len; i++)
    {
      String current = aStrings[i].intern();
      if (contains(current))
      {
        throw new IllegalArgumentException("Duplicate field name '" + current
            + "'");
      }
      theValues[i] = current;
    }
  }

  public String get(int aIndex) throws IndexOutOfBoundsException
  {
    return theValues[aIndex];
  }

  @Override
  public String get(Object aKey)
  {
    return theValues[internedNameIndex(aKey.toString().intern())];
  }

  /**
   * Quick find of offset for aInternedName via an identity compare rather than
   * String.equals(). As the method name indicates, the String MUST be interned
   * to work correctly.
   * 
   * @param aInternedName
   * @return
   */
  public final int internedNameIndex(String aInternedName)
  {
    String[] fields = theValues;
    int length = fields.length;
    for (int i = 0; i < length; i++)
    {
      if (aInternedName == fields[i])
      {
        return i;
      }
    }
    throw new IllegalArgumentException(aInternedName);
  }

  @Override
  public boolean isAlterable()
  {
    return false;
  }

  @Override
  public Iterator<String> iterator()
  {
    return new IArrayIterator<>(theValues);
  }

  @Override
  public Set<String> keySet()
  {
    return this;
  }

  @Override
  public String set(String aKey, String aValue)
      throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size()
  {
    return theValues.length;
  }
}
