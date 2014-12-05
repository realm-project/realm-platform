package net.objectof.model;

import net.objectof.Named;

/**
 * An Id identifies a Resource object. Resources are uniquely identified through
 * the unique combination of a Kind and label object. Labels are expected to be
 * simple objects such as Numbers or Strings.
 * <p>
 * It is strongly recommended that when implementing an Id, the equals() method
 * performs:
 * <p>
 * {@code this.kind().equals(a.kind()) && this.label().equals(a.label())}
 * <p>
 * As per Java's general recommendation, the hashCode() method should be
 * overridden as well.
 * 
 * @author jdh
 * 
 */
public interface Id<T> extends Named
{
  /**
   * @return The Kind of object.
   */
  Kind<T> kind();

  /**
   * @return The object's label.
   */
  Object label();
  
}
