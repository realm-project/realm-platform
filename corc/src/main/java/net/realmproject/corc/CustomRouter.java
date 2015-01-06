package net.realmproject.corc;

import java.io.IOException;
import java.util.Map;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.util.IRouter;

public abstract class CustomRouter<T> extends IRouter {

	public CustomRouter(Map<String, Handler<Object>> aMap) {
		super(aMap);
	}
	
	@Override
	protected final void onExecute(Action aRequest, Object aObject) {
		
		T tRequest = (T) aObject;
		String channelName = route(aRequest, tRequest);
		
		if (channelName == null) {
			throw new IllegalArgumentException("Invalid Channel Name");
		} else {
			Handler<Object> handler = getChain(channelName);
			String newActionName = name(aRequest, tRequest);
			handler.execute(newAction(aRequest, newActionName), aObject);
		}
	}
	
	/**
	 * Determines which route to follow
	 */
	protected abstract String route(Action aRequest, T object);
	
	/**
	 * Determines the name of the new action passed to the next handler
	 */
	protected String name(Action aRequest, T object) {
		return route(aRequest, object);
	}
	
}
