package net.objectof.ext;

import net.objectof.Selector;

/**
 * A View provides a common general enumerable view of an object. The following
 * loop illustrates the means to enumerate the view:
 *
 * <pre>
 * for (Object k : aView.keySet())
 * {
 *   V value = aView.get(k);
 * }
 * </pre>
 *
 * @author jdh
 *
 * @param <V>
 */
@Selector
public interface View<V>
{
  /**
   * A Set View. Implementations must return 'this' for keySet().
   * Implementations must ensure the same object via equals() is contained only
   * once. The (not null) object returned ("e") in get() must return true for
   * aKey.equals(e).
   *
   * @author jdh
   *
   * @param <T>
   */
  public interface Viewset<T> extends View<T>, Iterable<T>
  {
  }

  /**
   * @return An object associated to aKey or null.
   *
   * @throws RuntimeException
   *           An exception was raised by an underlying implementation.
   */
  // TODO change the selector to "@".
  @Selector("at:")
  V get(Object aKey) throws RuntimeException;

  /**
   * @return The set of keys exposed by this view.
   */
  @Selector("keys")
  Viewset<?> keySet();
}
