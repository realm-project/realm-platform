package net.objectof.repo.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

public abstract class IRepoText
{
  private final HashMap<String, Long> theIds = new HashMap<String, Long>();
  private final HashMap<Long, String> theStrings = new HashMap<Long, String>();

  public IRepoText()
  {
  }

  public final String get(Long aId)
  {
    String chars = theStrings.get(aId);
    if (chars == null)
    {
      synchronized (this)
      {
        chars = load(aId);
        add(aId, chars);
      }
    }
    return chars;
  }

  public final Long get(String aChars)
  {
    Long id = theIds.get(aChars);
    if (id == null)
    {
      synchronized (this)
      {
        id = find(aChars);
        if (id == null)
        {
          id = define(aChars);
        }
        add(id, aChars);
      }
    }
    return id;
  }

  public Reader getReader(Long aId)
  {
    if(theStrings.containsKey(aId))
    {
      return new StringReader(get(aId));
    }
    return openText(aId);
  }

  protected abstract Reader openText(Long aId);

  protected abstract Long define(String aChars);

  protected abstract Long find(String aChars);

  protected abstract String load(Long aId);

  private final void add(Long aId, String aChars)
  {
    theIds.put(aChars, aId);
    theStrings.put(aId, aChars);
  }
}
