package net.objectof.model;

import net.objectof.ext.Archetype;

/**
 * Stereotypes define the elemental types. Stereotypes define concrete
 * <em>intent</em> rather than implementation: they categorize data with
 * implicit or assumed attributes / behaviors. Stereotypes, by themselves, are
 * not within a class hierarchy and are independent value domains.
 * <p>
 * Stereotypes are used to categorize types, specifically a {@link Kind}.
 * <p>
 * Each Stereotype is documented below with:
 * <ul>
 * <li><b>assume</b>: The assumed Java type in the absence of a specific
 * contract.
 * <li><b>json</b>: The relationship to the Stereotype and JSON types.
 * </ul>
 *
 * @author jdh
 *
 */
public enum Stereotype
{
  /**
   * Models existence.
   *
   * @assume java.util.Void
   * @json = null.
   */
  NIL(Archetype.EMPTY),
  /**
   * Any aggregate whose values are associated to and accessible through a key;
   * an associative array; a map.
   *
   * @assume java.util.Map
   * @json = Object.
   */
  MAPPED(Archetype.CONTAINER),
  /**
   * Any aggregate whose values are sequenced and associated to and accessible
   * through an integer index, i.e. array, vector, or list.
   *
   * @assume java.util.List
   * @json = Array
   */
  INDEXED(Archetype.CONTAINER),
  /**
   * A sequence of Unicode values.
   * <p>
   * Text may be implemented through a String, sequence (e.g. CharSequence), or
   * a stream (e.g. Reader).
   *
   * @assume java.lang.String
   * @json = String
   */
  TEXT(Archetype.SERIAL),
  /**
   * An integer, generally implemented as a 64-bit two's complement signed
   * binary value.
   *
   * @assume java.lang.Long
   * @json = Number without fraction or exponent.
   */
  INT(Archetype.SCALAR),
  /**
   * A real number, generally implemented as a 64-bit (double) precision
   * floating point value.
   *
   * @assume java.lang.Double
   * @json = Number with fraction or exponent.
   */
  NUM(Archetype.SCALAR),
  /**
   * A two-state value, generally implemented as a boolean.
   * <p>
   * When implementing from data potentially holding more than two states:
   * "false" is to be implemented as "off/zero/null" with "true" being !false
   * (not false). Generally a falsehood has better semantics than a truth.
   *
   * @assume java.lang.Boolean
   * @json = <u>true</u> <u>false</u>
   */
  BOOL(Archetype.SCALAR),
  /**
   * Any tuple/structure, record (which is defined here as a tuple whose
   * elements are mapped rather than indexed), or object. A Composed entries are
   * fixed and set by the type.
   * <p>
   * Use COMPOSED for classes that are "opaque" to the model, such as an
   * implementation class that needs to be referenced but not otherwise modeled.
   *
   * @assume java.lang.Object
   * @json Object - unmarshalling requires a schema.
   */
  COMPOSED(Archetype.CONTAINER),
  /**
   * A reference to an entity. An entity is defined here as any "whole" data
   * structure rather than a <em>part</em> of an aggregation. The model supports
   * the distinction (even though, for example, Java doesn't). As an example,
   * consider a JSON document: The document is-a entity. Parts of the entity are
   * included in the document itself while referenced Entities are referenced in
   * some form (through a string or reference-style JSON object).
   *
   * @assume java.lang.Object
   * @json String or Object - unmarshalling requires a schema for String
   *       representation. For object representation, the unmarshaller can
   *       recognize reference objects. Assume marshalling is a String
   *       identifying/naming the object or a reference to it, such as an URI.
   */
  REF(Archetype.SCALAR),
  /**
   * A function or functional object; a method; a web service.
   *
   * @assume java.lang.Object
   * @json String or Object - unmarshalling requires a schema for String
   *       representation. For object representation, the unmarshaller can
   *       recognize function references. Assume marshalling is a String
   *       identifying/naming the function or a reference to it, such as an URI.
   *       Note that the intent of representation of functions in JSON should
   *       generally be to <em><b>describe</b></em> or reference functionality,
   *       and generally <em>not</em> to transfer code itself.
   */
  FN(Archetype.SCALAR),
  /**
   * A Set.
   *
   * @assume java.util.Set
   * @json Array or Object - unmarshalling requires a schema. Default
   *       marshalling is Array.
   */
  SET(Archetype.CONTAINER),
  /**
   * A time based value reporting a UTC minute. Implementations using a
   * "Unix/Java long" MUST truncate or otherwise ignore sub-minute values. To
   * provide a sub-minute point in time, define an (INT|NUM) to capture the
   * desired precision.
   *
   * @assume java.util.Date
   * @json String - unmarshalling requires a schema.
   */
  MOMENT(Archetype.SCALAR),
  /**
   * Media represents an "opaque object" nominally consisting of a sequence of
   * octets/bytes. Use Media only for true media types or an
   * implementation-dependent class that requires state transfer (serialization)
   * but should not otherwise be modeled as empty composites.
   *
   * @assume java.lang.Object
   * @json String. unmarshalling requires a schema.
   */
  MEDIA(Archetype.SERIAL);
  private Archetype theModel;

  private Stereotype(Archetype aModel)
  {
    theModel = aModel;
  }

  public Archetype getModel()
  {
    return theModel;
  }
}
