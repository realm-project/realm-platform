package net.realmproject.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.objectof.aggr.Mapping;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.query.Query;

public class RealmRepo {

	
	public static <T extends Resource<T>> T load(Supplier<T> getter, Supplier<T> creator, Consumer<T> setter, Transaction pureTx, String kind) {
		T t = load(getter, creator, setter);
		return pureTx.retrieve(kind, t.id().label().toString());
	}
	
	/**
	 * Retrieves something of type T, creating it if it is null
	 * @param getter function to get T 
	 * @param creator function to create T
	 * @param setter function to set T in source data structure
	 * @return T
	 */
	public static <T> T load(Supplier<T> getter, Supplier<T> creator, Consumer<T> setter) {
		T t = getter.get();
		if (t != null) {return t;}
		
		t = creator.get();
		setter.accept(t);
		
		return t;
	}
	
	public static <T> T load(Mapping<String, T> map, String key, Supplier<T> creator) {
		T t = map.get(key);
		if (t != null) {return t;}
		
		t = creator.get();
		map.put(key, t);
		
		return t;
	}

	
	public static <T> T queryHead(Transaction tx, String kind, Query q) {
		Iterable<T> ts = tx.query(kind, q);
		
		if (!ts.iterator().hasNext()) return null;
		
		return ts.iterator().next();
	}
	
}
