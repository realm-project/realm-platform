package net.realmproject.security.authorization.authorizers;

import java.util.Collections;
import java.util.List;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.model.schema.Person;
import net.realmproject.security.authorization.RealmAuthorizers;

public class RepoClassname extends RealmAuthorizers {

	private List<String> classes;
	
	public RepoClassname(String cls) {
		this(Collections.singletonList(cls));
	}
	
	public RepoClassname(List<String> classes) {
		this.classes = classes;
	}

	@Override
	public boolean authorize(Action action, HttpRequest request, Person person) {
	
		try {
			String className = className(action, request);
			return classes.contains(className);
		}
		catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
