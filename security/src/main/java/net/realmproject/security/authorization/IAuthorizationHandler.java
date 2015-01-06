package net.realmproject.security.authorization;


import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.corc.DatabaseRepository;
import net.realmproject.model.schema.Person;
import net.realmproject.security.ISessionHandler;

public class IAuthorizationHandler extends ISessionHandler {

	private Authorizer auth;
	
	public IAuthorizationHandler(Handler<?> aDefault, DatabaseRepository dbrepo, Authorizer auth) {
		super(aDefault, dbrepo);
		this.auth = auth;
	}

	
	@Override
	protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {
				
		if (auth.authorize(action, request, person)) {
			chain(action, request);
		} else {
			request.getHttpResponse().sendError(403);
		}
	}

}
