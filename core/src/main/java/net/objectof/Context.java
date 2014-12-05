package net.objectof;


/**
 * A Context can emit objects associated to a <em>name</em>. Generally the names
 * are <em>component names</em> as defined in {@link Named}. A Context that:
 * <ul>
 * <li>contains a set of named persistent or singleton objects is known as a
 * <em>scope</em>
 * <li>creates new objects based on a name is known as a <em>factory</em>.
 * </ul>
 * A Context can be both a scope and factory.
 *
 * @author jdh
 *
 * @param <T>
 *            The type of object emitted by the Context.
 */
public interface Context<T> extends Named {

    /**
     * @param aComponentName
     *            A name identifying the singleton or a type of instance to
     *            return.
     * @return An object for aComponentName or null. This interface doesn't
     *         specify any constraints on the object that is returned for each
     *         invocation: i.e. there is no requirement that the same object or
     *         null or a different object will be returned for repeated calls to
     *         this even when the <b>same name</b> is specified.
     * @throws InvalidNameException
     *             When aComponentName is not known by this Context.
     * @throws ClassCastException
     *             When this Context reifies the generic type and the name
     *             references an object whose type is not assignable to the
     *             reified type.
     */
    T forName(String aComponentName);
}
