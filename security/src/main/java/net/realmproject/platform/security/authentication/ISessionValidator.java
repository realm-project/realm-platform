package net.realmproject.platform.security.authentication;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Person;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.security.ISessionHandler;

/**
 * Validates a current session, to make sure that the user has logged in. 
 * @author nathaniel
 *
 */
public class ISessionValidator extends ISessionHandler {

	public ISessionValidator(Handler<?> aDefault, DatabaseRepository dbrepo) {
		super(aDefault, dbrepo);
	}
	
	@Override
	protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {
		
		if (person == null) {
			//401 Unauthorized: "specifically for use when authentication is required and has failed or has not yet been provided"
			request.getHttpResponse().sendError(401);
			return;
		}
				
		chain(action, request); 

	}


}
