package net.realmproject.security.authorization.authorizers;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Person;
import net.realmproject.security.authorization.Authorizer;

public class Permissive implements Authorizer {

	public Permissive() {}
	
	@Override
	public boolean authorize(Action action, HttpRequest request, Person person) {
		return true;
	}

}
