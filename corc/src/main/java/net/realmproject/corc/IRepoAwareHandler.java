package net.realmproject.corc;

import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.objectof.model.Package;

public class IRepoAwareHandler extends IHandler<HttpRequest> implements RepoAware {

	private final Package repo;
	
	public IRepoAwareHandler(DatabaseRepository dbrepo) {
		repo = dbrepo.get();
	}
	
	public IRepoAwareHandler(Handler<?> aDefault, DatabaseRepository dbrepo) {
		super(aDefault);
		repo = dbrepo.get();
	}
	
	
	
	
	@Override
	public Class<? extends HttpRequest> getArgumentClass() {
		return HttpRequest.class;
	}
	
	@Override
	public Package repo() {
		return repo;
	}
	

}
