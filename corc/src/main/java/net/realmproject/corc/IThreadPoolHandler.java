package net.realmproject.corc;


import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.util.RealmThread;

public class IThreadPoolHandler extends IHandler<HttpRequest>{

	
	public IThreadPoolHandler(Handler<?> aDefault) {
		super(aDefault);
	}
	
	@Override
	public Class<? extends HttpRequest> getArgumentClass() {
		return HttpRequest.class;
	}
	
	protected void onExecute(Action action, HttpRequest request) throws Exception {
		RealmThread.getThreadPool().submit(() -> chain(action, request));
	}

}
