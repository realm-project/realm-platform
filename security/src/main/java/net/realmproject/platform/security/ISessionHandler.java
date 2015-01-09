package net.realmproject.platform.security;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.model.schema.Person;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.util.RealmCorc;


public abstract class ISessionHandler extends IRepoAwareHandler {

	
	public ISessionHandler(DatabaseRepository dbrepo) {
		super(dbrepo);
	}
	
	public ISessionHandler(Handler<?> aDefault, DatabaseRepository dbrepo) {
		super(aDefault, dbrepo);
	}
	
	@Override
	protected final void onExecute(Action action, HttpRequest request) throws Exception {
		Transaction tx = repo().connect(ISessionHandler.class.getName());
		onExecute(action, request, RealmCorc.getUser(tx, request));
		tx.close();
	}
	
	protected abstract void onExecute(Action action, HttpRequest request, Person person) throws Exception;
		
}
