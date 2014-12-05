package net.objectof.model.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import net.objectof.model.Package;

/**
 * Query created by composing several other Queries using a single
 * {@link Operator}. Queries with combinations of different Operators should use
 * nested ICompositeQuerys
 * 
 * @author NAS
 *
 */
public class ICompositeQuery implements Query
{
  private final Collection<Query> queries = new ArrayList<>();
  private final Operator op;

  public ICompositeQuery()
  {
    this(Operator.AND);
  }

  public ICompositeQuery(Operator op)
  {
    this.op = op;
  }

  public ICompositeQuery(Operator op, Query... queries)
  {
    this.op = op;
    addAll(Arrays.asList(queries));
  }

  public ICompositeQuery(Query... queries)
  {
    this(Operator.AND, queries);
  }

  public boolean add(Query arg0)
  {
    return queries.add(arg0);
  }

  public boolean addAll(Collection<? extends Query> arg0)
  {
    return queries.addAll(arg0);
  }

  public void clear()
  {
    queries.clear();
  }

  public boolean contains(Object arg0)
  {
    return queries.contains(arg0);
  }

  public boolean containsAll(Collection<?> arg0)
  {
    return queries.containsAll(arg0);
  }

  public boolean isEmpty()
  {
    return queries.isEmpty();
  }

  public boolean remove(Object arg0)
  {
    return queries.remove(arg0);
  }

  public boolean removeAll(Collection<?> arg0)
  {
    return queries.removeAll(arg0);
  }

  @Override
  public Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver)
  {
    Set<String> results = null;
    Set<String> resolve = null;
    for (Query query : queries)
    {
      resolve = query.resolve(Integer.MAX_VALUE, aPackage, aKind, resolver);
      if (results == null)
      {
        results = resolve;
      }
      else
      {
        results = op.op(results, resolve);
      }
    }
    if (results == null)
    {
      results = new LinkedHashSet<>();
    }
    return IQuery.applyLimit(results, aLimit);
  }

  public boolean retainAll(Collection<?> arg0)
  {
    return queries.retainAll(arg0);
  }

  public int size()
  {
    return queries.size();
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Query q : queries)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        sb.append(" " + op.toString() + " ");
      }
      sb.append("(" + q.toString() + ")");
    }
    return sb.toString();
  }
}
