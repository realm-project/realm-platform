package net.realmproject.corc;

import java.io.IOException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;

public class IEchoHandler extends IHandler<HttpRequest>{
	
	public IEchoHandler() {
		super();
	}

	@Override
	public Class<? extends HttpRequest> getArgumentClass() {
		return HttpRequest.class;
	}
	
	@Override
	protected void onExecute(Action aRequest, HttpRequest request) throws IOException {
		
		request.getWriter().write("Echo");
		
	}
	

}
