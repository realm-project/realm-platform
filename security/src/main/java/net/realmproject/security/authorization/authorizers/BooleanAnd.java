package net.realmproject.security.authorization.authorizers;

import java.util.ArrayList;
import java.util.List;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Person;
import net.realmproject.security.authorization.Authorizer;

public class BooleanAnd implements Authorizer {

	private List<Authorizer> auths;

	public BooleanAnd(Authorizer a1) { 
		auths = new ArrayList<>(); 
		auths.add(a1); 
	}
	
	public BooleanAnd(Authorizer a1, Authorizer a2) { 
		auths = new ArrayList<>(); 
		auths.add(a1); 
		auths.add(a2);
	}
	
	public BooleanAnd(Authorizer a1, Authorizer a2, Authorizer a3) { 
		auths = new ArrayList<>(); 
		auths.add(a1); 
		auths.add(a2);
		auths.add(a3); 
	}
	
	public BooleanAnd(List<Authorizer> auths) {
		this.auths = auths;
	}
	
	@Override
	public boolean authorize(Action action, HttpRequest request, Person person) {
		for (Authorizer auth : auths) {
			if (!auth.authorize(action, request, person)) return false;
		}
		return true;
	}

	
	
}
