package net.objectof;


/**
 * A Type is a Named object that classifies objects. Type provides a minimal
 * interface into typing. Types in different contexts with the same relative
 * component names may represent the same concept within different
 * implementations or facets.
 *
 * @author jdh
 */

public interface Type extends Named {

    /**
     * @return true when this Type considers aObject to be of this type.
     */
    public boolean isInstance(Object aObject);
}
