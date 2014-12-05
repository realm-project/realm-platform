package net.objectof.model.query;

import java.util.List;
import java.util.Set;

import net.objectof.model.Package;

/**
 * Defines how to resolve simple <tt>key o value</tt> relationships using a
 * specific {@link Package} implementation. More complex queries are resolved
 * using set operations on the results of these simple <tt>key o value</tt>
 * queries
 * 
 * @author NAS
 *
 */
public interface QueryResolver {

	/**
	 * Examines keyed elements within a Package to find ones which satisfy a
	 * {@link Relation} with the given value. Kind is implicit in this
	 * resolution, as it should remain constant through a higher level of
	 * resolution.
	 * 
	 * @param key
	 *            the key to match against
	 * @param relation
	 *            the {@link Relation} the given value should satisfy with the
	 *            values being examined
	 * @param value
	 *            the value to compare the examined values against
	 * @return a {@link List} of Strings representing the Label of the matched
	 *         elements
	 */
	Set<String> resolve(int aLimit, String key, Relation relation, Object value) throws UnsupportedOperationException,
			IllegalArgumentException;

}
