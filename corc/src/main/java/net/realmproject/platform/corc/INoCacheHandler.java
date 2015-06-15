package net.realmproject.platform.corc;


import java.io.IOException;

import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;


public class INoCacheHandler extends IHandler<HttpRequest> {

    public INoCacheHandler(Handler<?> aDefault) throws ConnectorException {
        super(aDefault);
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    protected void onExecute(Action aRequest, HttpRequest request) throws IOException {
        request.getHttpResponse().setHeader("Cache-Control", "no-cache,no-store,max-age=0");
        request.getHttpResponse().setHeader("Pragma", "No-cache");
        request.getHttpResponse().setDateHeader("Expires", 1);
        chain(aRequest, request);
    }

}
