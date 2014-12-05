package net.objectof.model.query;

import java.util.Set;

import net.objectof.model.Package;

/**
 * Constructs queries in Conjunctive Normal Form
 * @author NAS
 *
 */
public class CNFQueryBuilder implements Query {

	private ICompositeQuery and;
	private ICompositeQuery or;
	
	
	public CNFQueryBuilder() {
		and = new ICompositeQuery(Operator.AND);
		newOr();
	}
	
	public CNFQueryBuilder(Query start) {
		this();
		or.add(start);
	}

	public CNFQueryBuilder(String key, Object value) {
		and = new ICompositeQuery(Operator.AND);
		newOr();
		or.add(new IQuery(key, value));
	}
	
	public CNFQueryBuilder(String key, Relation relation, Object value) {
		and = new ICompositeQuery(Operator.AND);
		newOr();
		or.add(new IQuery(key, relation, value));
	}
	
	
	
	
	public CNFQueryBuilder or(Query query) {
		or.add(query);
		return this;
	}
	
	public CNFQueryBuilder or(String key, Object value) {
		return or(new IQuery(key, value));
	}
	
	public CNFQueryBuilder or(String key, Relation relation, Object value) {
		return or(new IQuery(key, relation, value));
	}
	
	
	
	
	public CNFQueryBuilder and(Query query) {
		newOr();
		or(query);
		return this;
	}
	
	public CNFQueryBuilder and(String key, Object value) {
		return and(new IQuery(key, value));
	}

	public CNFQueryBuilder and(String key, Relation relation, Object value) {
		return and(new IQuery(key, relation, value));
	}
	
	
	
	public Query build() {
		return and;
	}
	
	
	private void newOr() {
		or = new ICompositeQuery(Operator.OR);
		and.add(or);
	}

	@Override
	public Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver) {
		return build().resolve(aLimit, aPackage, aKind, resolver);
	}
	
}
