package net.objectof.ext;

import net.objectof.Selector;

/**
 * A Vector is a minimal interface into an indexed sequence of objects.
 * 
 * @author jdh
 *
 * @param <T>
 */
@Selector
public interface Vector<T>
{
  @Selector("#")
  public T get(int aIndex);

  @Selector("size")
  public int size();
}
