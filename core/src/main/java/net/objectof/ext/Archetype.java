package net.objectof.ext;

public enum Archetype
{
  /**
   * An empty sequence. JSON null is empty?
   */
  EMPTY,
  /**
   * All elements consist of Characters. Text has characteristics of both a
   * Sequence and a Scalar.
   */
  SERIAL,
  /**
   * A single scalar value, including a reference to an entity/object.
   */
  SCALAR,
  /**
   * All elements consist of a single type. Containers may be mapped, composite,
   * sequenced, and set aggregates.
   */
  CONTAINER,
  /**
   * A mixture of various models. Mixed is a sequence or set.
   */
  MIXED;
}
