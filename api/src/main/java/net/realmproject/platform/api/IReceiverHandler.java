package net.realmproject.platform.api;


import net.objectof.InvalidNameException;
import net.objectof.Receiver;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmLog;

import org.apache.commons.logging.Log;


public class IReceiverHandler extends ISessionHandler {

    private Log log = RealmLog.getLog();
    private Receiver receiver;

    public IReceiverHandler(DatabaseRepository dbrepo, Receiver target) {
        super(dbrepo);
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
