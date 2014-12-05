package net.objectof.model.query;

import java.util.Set;

import net.objectof.model.Package;

/**
 * Interface representing a specific search query against a {@link Package}.
 * @author NAS
 *
 */

public interface Query {

	/**
	 * Transforms this query into a result set using the given {@link QueryResolver}
	 * @param resolver
	 * @return A Set of Strings representing the label of the records found by this query.
	 */
	Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver);
	
}
