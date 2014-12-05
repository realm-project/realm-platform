package net.objectof.model.query;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines the relationship between two {@link Query}s where those queries are
 * parts of a larger query.
 *
 * @author NAS
 *
 */
public enum Operator
{
  AND
  {
    @Override
    public Set<String> op(Set<String> a, Set<String> b)
    {
      Set<String> s = new HashSet<>();
      s.addAll(a);
      s.retainAll(b);
      return s;
    }
  },
  OR
  {
    @Override
    public Set<String> op(Set<String> a, Set<String> b)
    {
      Set<String> s = new HashSet<>();
      s.addAll(a);
      s.addAll(b);
      return s;
    }
  };
  public Set<String> op(Set<String> a, Set<String> b)
  {
    return null;
  }
}
