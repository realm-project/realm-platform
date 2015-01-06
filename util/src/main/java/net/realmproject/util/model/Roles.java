package net.realmproject.util.model;

import net.objectof.model.Transaction;
import net.objectof.model.query.IQuery;
import net.realmproject.model.schema.Role;
import net.realmproject.util.RealmRepo;

public class Roles {

	public static Role get(Transaction tx, String roleName) {
		return RealmRepo.queryHead(tx, "Role", new IQuery("name", roleName));
	}
	
}
