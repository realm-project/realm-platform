package net.objectof.aggr.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.objectof.aggr.Set;
import net.objectof.rt.impl.IFn;

public class IIntRange extends IAggregate<Integer, Integer> implements
    Set<Integer>
{
  private final static class Iter extends IFn implements
      Iterator<Integer>
  {
    private int theCurrent;
    private final int theEnd;

    public Iter(int aStart, int aEnd)
    {
      theCurrent = aStart;
      theEnd = aEnd;
    }

    @Override
    public boolean hasNext()
    {
      return theCurrent < theEnd;
    }

    @Override
    public Integer next()
    {
      if (theCurrent >= theEnd)
      {
        throw new NoSuchElementException();
      }
      return theCurrent++;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private final int theStart;
  private final int theEnd;

  public IIntRange(int aStart, int aEnd)
  {
    super(Integer.class, Integer.class);
    theStart = aStart < aEnd ? aStart : aEnd;
    theEnd = aEnd > aStart ? aEnd : aStart;
  }

  @Override
  public boolean contains(Object aInt)
  {
    if (aInt instanceof Integer)
    {
      int arg = (Integer) aInt;
      return arg >= theStart && arg <= theEnd;
    }
    else
    {
      return false;
    }
  }

  @Override
  public Integer get(Object aKey)
  {
    int key = (Integer) aKey;
    if (key >= theStart && key <= theEnd)
    {
      return (Integer) aKey;
    }
    throw new IndexOutOfBoundsException();
  }

  @Override
  public boolean isAlterable()
  {
    return false;
  }

  @Override
  public Iterator<Integer> iterator()
  {
    return new Iter(theStart, theEnd);
  }

  @Override
  public Set<Integer> keySet()
  {
    return this;
  }

  @Override
  public Integer set(Integer aKey, Integer aValue)
  {
    throw new IllegalStateException();
  }

  @Override
  public int size()
  {
    return Math.abs(theEnd - theStart);
  }
}
