package net.realmproject.platform.corc;

import net.objectof.Context;
import net.objectof.model.Package;

public class DatabaseRepository {

	private Context<Package> db;
	private String repo;
	
	public DatabaseRepository(Context<Package> db, String repo) {
		this.db = db;
		this.repo = repo;
	}
	
	public Package get() {
		return db.forName(repo);
	}
	
}
