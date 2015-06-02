package net.realmproject.platform.corc;


import java.io.IOException;
import java.util.Map;

import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmError;


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
        return requestName + "/" + channelName;
    }

    protected void noChannel(Action aRequest, HttpRequest object) {
        try {
            RealmError.send(object, 404, "Path not found.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
