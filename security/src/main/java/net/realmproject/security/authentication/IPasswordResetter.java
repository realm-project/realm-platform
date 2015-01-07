package net.realmproject.security.authentication;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.corc.DatabaseRepository;
import net.realmproject.corc.IRepoAwareHandler;
import net.realmproject.model.schema.Person;
import net.realmproject.util.RealmAuthentication;
import net.realmproject.util.RealmCorc;
import net.realmproject.util.RealmSerialize;
import net.realmproject.util.model.Persons;

/**
 * Handles a user resetting their password if they forget it.
 * @author nathaniel
 *
 */

public class IPasswordResetter extends IRepoAwareHandler {

	public IPasswordResetter(DatabaseRepository dbrepo) {
		super(dbrepo);
	}
	


	@Override
	protected void onExecute(Action action, HttpRequest request) throws Exception {
		
		String json = RealmCorc.getJson(request.getHttpRequest().getReader());
		Username username = RealmSerialize.deserialize(json, Username.class);
		

		String temporaryPassword = RealmAuthentication.generateTemporaryPassword();
		String temporarySalt = RealmAuthentication.generateSalt();
		String hashedTemporaryPassword = RealmAuthentication.hash(temporaryPassword, temporarySalt);
		
		Transaction tx = repo().connect(username.username);
		Person person = Persons.fromUsername(tx, username.username);
		person.setPwdHashed(hashedTemporaryPassword);
		person.setSalt(temporarySalt);
		
		tx.post();
		tx.close();
		
		//TODO: Send email containing temporary password
	}


}