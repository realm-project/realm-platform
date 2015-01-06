package net.realmproject.security.authentication;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.corc.DatabaseRepository;
import net.realmproject.model.schema.Person;
import net.realmproject.util.RealmAuthentication;

/**
 * Handles changing a user's password. Requires them to re-authenticate with information described in the PasswordChange class
 * @author nathaniel
 *
 */

public class IPasswordChanger extends AbstractAuthenticator<PasswordChange> {

	public IPasswordChanger(DatabaseRepository dbrepo) {
		super(dbrepo, PasswordChange.class);
	}

	@Override
	protected void onAuthenticate(Action action, HttpRequest request, Person person, PasswordChange info, boolean success) throws Exception {
		
		if (success) {
					
			String newSalt = RealmAuthentication.generateSalt();
			String hashedNewPassword = RealmAuthentication.hash(info.newPassword, newSalt);
			
			person.setPwdHashed(hashedNewPassword);
			person.setSalt(newSalt);
			
			person.tx().post();
			
		} else {
			//401 Unauthorized: "specifically for use when authentication is required and has failed or has not yet been provided"
			request.getHttpResponse().sendError(401);
		}	
	}
	

}
