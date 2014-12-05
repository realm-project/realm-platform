package net.objectof;


/**
 * A Named object has an absolute, unique, uniform name in the {@code ans://}
 * naming scheme. {@code ans://} is a (currently unregistered) URI scheme that
 * conforms to a subset of {@code http://} syntax.
 * <p>
 * The purpose of ans is to provide a URI naming system that defines a
 * relatively simple, flexible, <em>canonical</em>, and <em>federated</em>
 * identification scheme. Unlike {@code http://} that specifies a name or
 * location on a network, ans specifies a location-independent name. However,
 * using this name, you can obtain objects within a given <em>dominion</em> of
 * objectof.
 * <p>
 * The following is the ABNF (rfc5234) syntax:
 *
 * <pre>
 * ansName      = "ans://" absoluteName
 * absoluteName = authority [pathName ["-" label]]
 * authority    = domain ":" release
 * pathName     = 1*("/" componentName)
 * label        = 1*(ALPHA / esc / DIGIT)
 * componentName= *(name ".") name
 * name         = (ALPHA / esc) *(ALPHA / esc / DIGIT)
 * domain       = 1*(name ".") name
 * release      = 5DIGIT / 4DIGIT
 * esc          = "__" / "_" 2HEX
 * HEX          = DIGIT / %x41-46; uppercase A-F only
 * </pre>
 *
 * <p>
 * The {@code authority} specifies a unique initial context over time. The
 * {@code domain} identifies a registered Internet domain or sub-domain thereof.
 * The {@code release} value (the "URL port") defines the week in which the
 * authority is defined. The four-digit form is interpreted as YYWW where YY is
 * the year within the 21st century and WW is the week number within the year.
 * The five-digit form is interpreted as YYYWW for releases beyond the 21st
 * century. A <em>dominion</em> is a physical union of multiple authorities and
 * can be considered a view of the objectof web.
 * <ul>
 * <li>Domains must be defined by the registered owner of the domain, or a proxy
 * thereof.
 * <li>The release value <b>must</b> lie within a week in which the domain
 * holder is the registered owner of the domain. Note that "{@code WW}",
 * although specifying a week, can be any value between 00 and 99 provided it
 * follows the domain ownership restriction. Each release within a domain may be
 * totally independent from other releases within the same domain. A release can
 * be considered a major version change with no assurance of compatibility from
 * other releases: A release shouldn't equate to a version hence a week-by-week
 * identifier should provide an adequate time division.
 * </ul>
 * <p>
 * The {@code pathName} specifies a named path to an object. pathName components
 * may be a compound {@code componentName} or a simple {@code name}. The
 * hierarchy within a component name, if any, is opaque for the purposes of this
 * scheme and <em>should</em> be "little-endian".
 * <p>
 * Names are restricted to a small set of characters consisting of standard
 * Latin letters, digits, and the underscore. However, the underscore begins an
 * {@code esc} sequence enabling encoding of any other UTF code point by
 * specifying one or more byte values in sequence exactly representing a UTF-8
 * encoding of the code point. It is invalid to escape a character not requiring
 * escape, i.e. Latin letters and digits . To escape the "_" itself, use "_5F".
 * The intent is to have names be:
 * <ul>
 * <li>inherently canonical enabling simple string comparison for equality.
 * <li>directly represented in code since a {@code name} is a valid identifier
 * in many modern computer languages.
 * <li>essentially unambiguous from a human visual perspective. The small set of
 * code points promotes this.
 * </ul>
 */
public interface Named {

    /**
     * @return The absolute, unique name of the object.
     */
    public String getUniqueName();
}