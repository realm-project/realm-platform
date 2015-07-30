package net.realmproject.platform.api;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.objectof.InvalidNameException;
import net.objectof.Receiver;
import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.RealmResponse;


/**
 * Dispatches a received request into the given objectof {@link Receiver}
 * 
 * @author NAS
 *
 */
public class IReceiverHandler extends ISessionHandler {

    private Log log = LogFactory.getLog(getClass());
    private Receiver receiver;

    public IReceiverHandler(Connector connector, Receiver target) throws ConnectorException {
        super(connector);
        this.receiver = target;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, String username) throws Exception {

        String methodName = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

        log.debug(methodName);
        log.debug("Received method " + methodName + " for " + receiver.getClass().getSimpleName());
        log.debug(RealmCorc.getJson(request.getReader()));

        if (methodName == null) {
            RealmResponse.send(request, 400, "Method name cannot be null");
            return;
        }

        Transaction tx = null;
        try {
            tx = repo().connect(ISessionHandler.class.getName());
            Person person = RealmCorc.getUser(tx, request);
            this.receiver.perform(methodName, person, request);
        }
        catch (InvalidNameException e) {
            RealmResponse.send(request, 404, "No such method");
        }
        finally {
            if (tx != null) {
                tx.close();
            }
        }

    }

}
