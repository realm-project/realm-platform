package net.objectof.aggr;

import java.util.Map;

import net.objectof.Selector;

/**
 * A Map Aggregate.
 *
 * @author jdh
 *
 * @param <K>
 * @param <V>
 */
@Selector
public interface Mapping<K, V> extends Map<K, V>, Aggregate<K, V>
{
  @Override
  public net.objectof.aggr.Set<K> keySet();
}
