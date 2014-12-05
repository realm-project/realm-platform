package net.objectof.model.query;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import net.objectof.model.Package;
import net.objectof.model.query.fallback.FallbackResolver;

/**
 * Implementation of a simple <tt>key o value</tt> query
 * 
 * @author NAS
 *
 */
public class IQuery implements Query
{
  static <T> Set<T> applyLimit(Set<T> input, int aLimit)
  {
    if (input.size() <= aLimit)
    {
      return input;
    }
    Set<T> limited = new LinkedHashSet<>();
    int count = 0;
    for (T t : input)
    {
      limited.add(t);
      count++;
      if (count >= aLimit)
      {
        break;
      }
    }
    return limited;
  }

  private final String key;
  private final Object value;
  private final Relation relation;

  public IQuery(String key, Object value)
  {
    this(key, Relation.EQUAL, value);
  }

  public IQuery(String key, Relation relation, Object value)
  {
    this.key = key;
    this.value = value;
    this.relation = relation;
  }

  @Override
  public Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver)
  {
	  Set<String> results = new HashSet<>();
	  try {
		  results = resolver.resolve(aLimit, key, relation, value);
	  } catch (UnsupportedOperationException e) {
		  QueryResolver fallback = new FallbackResolver(aPackage, aKind);
		  results = fallback.resolve(aLimit, key, relation, value);
	  }
    return applyLimit(results, aLimit);
  }

  @Override
  public String toString()
  {
    return key + " " + relation.toString() + " " + value.toString();
  }
}
