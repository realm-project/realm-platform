package net.objectof.aggr;

import net.objectof.Selector;
import net.objectof.ext.View.Viewset;

/**
 * A Set Aggregate. The Aggregation key/value (T, T) association <em>must</em>
 * emit the same object.
 *
 * @author jdh
 *
 * @param <T>
 */
@Selector
public interface Set<T> extends Viewset<T>, java.util.Set<T>, Aggregate<T, T>
{
}
