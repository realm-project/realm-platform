package net.objectof.repo.impl.sqlite;

import java.io.Reader;

import net.objectof.model.Id;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.query.Query;

/**
 * Decorates transactions in order to work with databases 
 * which can only use a single connection at a time.
 * With all transactions using a single connection, all
 * operations such as post, query, etc... should be 
 * coordinated with synchronized blocks 
 * @author nathaniel
 *
 */

class SingleConnectionTxDecorator implements Transaction {

	private Transaction backer;
	private Object lock;

	public SingleConnectionTxDecorator(Transaction backer, Object globalLock) {
		this.backer = backer;
		this.lock = globalLock;
	}

	@Override
	public void close() {
		backer.close();
	}

	@Override
	public <T> T create(String aKind) {
		return backer.create(aKind);
	}

	@Override
	public <T> Iterable<T> enumerate(String kind)
			throws UnsupportedOperationException {
		
		synchronized (lock) {
			return backer.enumerate(kind);
		}
		
	}

	@Override
	public Package getPackage() {
		return backer.getPackage();
	}

	@Override
	public Status getStatus() {
		return backer.getStatus();
	}

	@Override
	public void post() throws IllegalStateException {
		
		synchronized (lock) {
			backer.post();
		}
	}

	@Override
	public <T> Iterable<T> query(String kind, Query query)
			throws IllegalArgumentException, UnsupportedOperationException {
		
		synchronized (lock) {
			return backer.query(kind, query);
		}
		
	}

	@Override
	public <T> T receive(String aMediaType, Reader aReader) {
		return backer.receive(aMediaType, aReader);
	}

	@Override
	public <T> T retrieve(String aKind, Object aLabel) {
		
		synchronized (lock) {
			return backer.retrieve(aKind, aLabel);
		}
	}

	@Override
	public <T> T retrieve(Id<?> aId) {
		
		synchronized (lock) {
			return backer.retrieve(aId);
		}
	}

	@Override
	public void setLimit(int aLimit) {
		backer.setLimit(aLimit);
	}

	@Override
	public <T> Iterable<T> query(String kind, String query) throws IllegalArgumentException,
			UnsupportedOperationException {
		return backer.query(kind, query);
	}

}
