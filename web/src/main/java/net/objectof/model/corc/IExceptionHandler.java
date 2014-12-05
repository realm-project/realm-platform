package net.objectof.model.corc;


import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;


public class IExceptionHandler extends IHandler<HttpRequest> {


    public IExceptionHandler(Handler<?> aDefault) {
        super(aDefault);
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    protected void onExecute(Action aRequest, HttpRequest aObject) throws Exception {
        try {
            chain(aRequest, aObject);
        }
        catch (Exception e) {
            aObject.getHttpResponse().sendError(500);
            throw e;
        }
    }

}
