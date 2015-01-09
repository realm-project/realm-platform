package net.realmproject.platform.security.authentication;


import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Person;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmLog;
import net.realmproject.platform.util.RealmSerialize;
import net.realmproject.platform.util.model.Persons;

import org.apache.commons.logging.Log;


public abstract class AbstractAuthenticator<T extends LoginInfo> extends IRepoAwareHandler {

	protected Log log = RealmLog.getLog();
	
	private Class<T> inputClass;

	public AbstractAuthenticator(DatabaseRepository dbrepo, Class<T> inputClass) {
		super(dbrepo);
		this.inputClass = inputClass;
	}
	
	public AbstractAuthenticator(Handler<?> aDefault, DatabaseRepository dbrepo, Class<T> inputClass) {
		super(aDefault, dbrepo);
		this.inputClass = inputClass;
	}
	
	@Override
	protected final void onExecute(Action action, HttpRequest request) throws Exception {
		
		String json = RealmCorc.getJson(request.getHttpRequest().getReader());
		T info = RealmSerialize.deserialize(json, inputClass);
		authenticate(action, request, info);
		
	}

	//synchronized here is a dirty hack to make sure that a person can't get around the authentication delay
	//and try and to parallelize a brute-force attack
	protected synchronized void authenticate(Action action, HttpRequest request, T info) throws Exception {
		
		//random delay to counter time-attacks
		RealmAuthentication.randomDelay();
		
		log.debug("Given username = " + info.username);
		
		//Look up the person by username and authenticate against given password
		Person person = Persons.fromUsername(repo().connect(info.username), info.username);
		boolean IsAuthenticated = authenticatePerson(person, info.password);
		
		log.debug("Authentication of " + info.username + " " + (IsAuthenticated ? "succeeded" : "failed"));
		
		//invoke authentication handler
		onAuthenticate(action, request, person, info, IsAuthenticated);
		
	}
	
	private boolean authenticatePerson(Person person, String password) throws InterruptedException {
		if (person == null) {
			log.debug("Person was null, not authenticating");
			return false;
		}
		return RealmAuthentication.authenticate(password, person.getPwdHashed(), person.getSalt());
	}
	
	
	
	protected abstract void onAuthenticate(Action action, HttpRequest request, Person person, T info, boolean success) throws Exception;
	
	
}
