package net.objectof.model.query;

import java.util.Iterator;

import net.objectof.model.Transaction;

public class QueryIterable<T> implements Iterable<T> {

	private Iterable<String> backing;
	private Transaction tx;
	private String kind;

	public QueryIterable(Transaction tx, String kind, Iterable<String> backing) {
		this.tx = tx;
		this.kind = kind;
		this.backing = backing;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private Iterator<String> iter = backing.iterator();
			
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				String label = iter.next();
				return (T) tx.retrieve(kind, label);
			}

			@Override
			public void remove() {
				iter.remove();
			}};
	}
	
	
	
	
}
