package net.realmproject.platform.api;


import net.objectof.InvalidNameException;
import net.objectof.Receiver;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmCorc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class IReceiverHandler extends ISessionHandler {

    private Log log = LogFactory.getLog(getClass());
    private Receiver receiver;

    public IReceiverHandler(Connector connector, Receiver target) throws ConnectorException {
        super(connector);
        this.receiver = target;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {

        String methodName = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

        log.debug(methodName);
        log.debug("Received method " + methodName + " for " + receiver.getClass().getSimpleName());
        log.debug(RealmCorc.getJson(request.getReader()));

        if (methodName == null) {
            request.getHttpResponse().sendError(404);
            return;
        }

        try {
            this.receiver.perform(methodName, person, request);
        }
        catch (InvalidNameException e) {
            request.getHttpResponse().sendError(400);
        }

    }

}
