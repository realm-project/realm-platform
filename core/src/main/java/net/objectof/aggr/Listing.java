package net.objectof.aggr;

import java.util.List;

import net.objectof.Selector;
import net.objectof.ext.Vector;

/**
 * A List Vector.
 *
 * @author jdh
 *
 * @param <T>
 */
@Selector
public interface Listing<T> extends Aggregate<Integer, T>, List<T>, Vector<T>
{
}
