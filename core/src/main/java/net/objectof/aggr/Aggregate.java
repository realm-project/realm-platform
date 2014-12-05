package net.objectof.aggr;

import net.objectof.Receiver;
import net.objectof.Selector;
import net.objectof.ext.View;

/**
 * Core aggregation interface. Aggregates are described in terms of member
 * elements. An element is a logical (key, value) pair, with standard generic
 * templates of &lt;K, V&gt;. Aggregate is-a Iterable of its element (V) values
 * and has-a Set of its element (K) keys.
 * <p>
 * Aggregates provide a minimal, single hierarchical view of collections and are
 * intended to work in conjunction with (supplement) the Java Collections
 * Framework (JCF), not to replace it. Implementors must ensure that extensions
 * do not conflict with (break) the JCF interfaces.
 * <p>
 * When an Aggregate is an Vector-based aggregation, the element key is referred
 * to as the element's <em>index</em>. When the aggregation is Composed, the
 * element's key is referred to as the element's <em>selector</em>.
 * <p>
 * Mutation methods in this hierarchy follow the general approach of the JCF
 * with regards to being optionally supported operations. Additionally, some
 * aggregates support the concept of being in alterable/non-alterable state.
 * When an alteration message is received by a supported operation, and the
 * aggregate is not in alterable state, an IllegalStateException is to be
 * thrown.
 *
 * @author jdh
 *
 * @param <K>
 * @param <V>
 */
/*
 * TODO re-add reification via the Interface type.
 *
 * Historical: All implementations of this hierarchy must support reification of
 * the key and element types through getElementType(), and therefore
 * keys().getElementType() as well. However it should be understood that the
 * Java &lt;K, V&gt; generics do not necessarily, and cannot be guaranteed to,
 * match the reification types. Specifically, all implementations must ensure
 * that all modification of element state occur as if the contract for the set()
 * method are performed: All other construction/initialization/modification
 * methods are to be considered convenience or optimization methods.
 * Sub-interfaces and implementations may <em>refine</em> the contract of set(),
 * but not break it. This constraint provides consumers with a quick reference
 * to the contracts regarding initialization and mutation.
 */
@Selector
public interface Aggregate<K, V> extends Iterable<V>, View<V>, Receiver
{
  /**
   * @param aKey
   * @return The element value at aKey or null.
   * @throws IllegalArgumentException
   *           when aKey is not valid for this aggregate.
   *
   */
  @Override
  /*
   * TODO define whether an implementation is required to throw an
   * IllegalArgumentException, or may optionally do so.
   */
  @Selector("at:")
  public V get(Object aKey);

  /**
   * @return A Set of element keys associated to this aggregation. Whether the
   *         keys are a view into the elements or not is undefined.
   */
  @Override
  @Selector("keys")
  public Set<K> keySet();

  /**
   * Associates aValue or null to an element of this aggregation. Whether the
   * element is created, modified, or removed is not defined by this interface.
   * <p>
   * Sub-interfaces or implementations may throw exceptions in addition to those
   * specified here.
   *
   * @param aKey
   *          The element's key. When a non-null key would be added to the key
   *          set then keys().getElementType().isInstance(aKey) must answer
   *          true. This interfaces doesn't specify whether a null key is
   *          allowed.
   * @param aValue
   *          The element's value or null. getElementType().isInstance(aValue)
   *          must answer true for a non-null value.
   * @return The prior value held by the element when applicable or null when
   *         not.
   * @throws UnsupportedOperationException
   *           When modification is not supported by this aggregation.
   * @throws NullPointerException
   *           When the specified key or value is null and this aggregation does
   *           not permit null keys or values.
   * @throws IllegalArgumentException
   *           When some aspect of the specified key or value prevents the value
   *           being stored.
   * @throws IllegalStateException
   *           When the current state of this object prevents a modification.
   */
  @Selector("at:put:")
  public V set(K aKey, V aValue);

  /**
   * @return Return the size of this Aggregate or Integer.MAX_VALUE when the
   *         count of elements exceeds an integer range.
   */
  @Selector
  public int size();
}
