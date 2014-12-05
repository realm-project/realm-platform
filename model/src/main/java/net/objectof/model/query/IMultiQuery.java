package net.objectof.model.query;

import java.util.Arrays;
import java.util.Set;

import net.objectof.model.Package;

/**
 * Query implementation matching against multiple values for the same key.
 * 
 * @author NAS
 *
 */
public class IMultiQuery implements Query
{
  public static Query fromArray(String key, Object... values)
  {
    return new IMultiQuery(key, Arrays.asList(values));
  }

  private final Query backing;

  public IMultiQuery(String key, Iterable<?> values)
  {
    this(key, Relation.EQUAL, values);
  }

  public IMultiQuery(String key, Relation relation, Iterable<?> values)
  {
    ICompositeQuery query = new ICompositeQuery(Operator.OR);
    for (Object value : values)
    {
      query.add(new IQuery(key, relation, value));
    }
    backing = query;
  }

  @Override
  public Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver)
  {
    return backing.resolve(aLimit, aPackage, aKind, resolver);
  }
}
