package net.realmproject.platform.corc;

import java.util.Map;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;

public class IHttpMethodRouter extends CustomRouter<HttpRequest> {

	public IHttpMethodRouter(Map<String, Handler<Object>> aMap) {
		super(aMap);
	}

	@Override
	protected String route(Action aRequest, HttpRequest request) {
		return request.getHttpRequest().getMethod();
	}

	//don't change the action name
	@Override
	protected String name(Action aRequest, HttpRequest request) {
		return aRequest.getName();
	}
	
}
