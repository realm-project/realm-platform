package net.realmproject.platform.corc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.util.IRouter;
import net.realmproject.platform.util.RealmCorc;

/**
 * Router for handlers using Request objects
 * 
 * @author nathaniel
 *
 */
public class IPathRouter extends CustomRouter<HttpRequest> {

	private int discard; 
	
	public IPathRouter(Integer discard, Map<String, Handler<Object>> aMap) {
		super(aMap);
		this.discard = discard;
	}
	
	public IPathRouter(Map<String, Handler<Object>> aMap) {
		super(aMap);
	}

	@Override
	protected String route(Action aRequest, HttpRequest request) {
		
		String requestName = aRequest.getName();
		for (int i = 0; i < discard; i++) {
			requestName += "/" + RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		}
		String channelName = RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		return channelName;
	}

	@Override
	protected String name(Action aRequest, HttpRequest request) {
		
		String requestName = aRequest.getName();
		for (int i = 0; i < discard; i++) {
			requestName += "/" + RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		}
		String channelName = RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		return requestName +  "/" + channelName;
	}
	
	/*
	@Override
	protected void onExecute(Action aRequest, Object aObject) {
		
		HttpRequest request = (HttpRequest) aObject;
		
		String requestName = aRequest.getName();
		for (int i = 0; i < discard; i++) {
			requestName += "/" + RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		}
		String channelName = RealmCorc.getNextPathElement(requestName, request.getHttpRequest());
		
		if (channelName == null) {
			try {
				request.getHttpResponse().sendError(404);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Handler<Object> handler = getChain(channelName);
			String newActionName = requestName +  "/" + channelName;
			handler.execute(newAction(aRequest, newActionName), aObject);
		}
	}
	*/

}
