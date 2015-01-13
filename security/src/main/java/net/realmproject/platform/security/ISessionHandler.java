package net.realmproject.platform.security;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.platform.corc.IRepoAwareHandler;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.util.RealmCorc;


public abstract class ISessionHandler extends IRepoAwareHandler {

    public ISessionHandler(Connector connector) throws ConnectorException {
        super(connector);
    }

    public ISessionHandler(Handler<?> aDefault, Connector connector) throws ConnectorException {
        super(aDefault, connector);
    }

    @Override
    protected final void onExecute(Action action, HttpRequest request) throws Exception {
        Transaction tx = repo().connect(ISessionHandler.class.getName());
        onExecute(action, request, RealmCorc.getUser(tx, request));
        tx.close();
    }

    protected abstract void onExecute(Action action, HttpRequest request, Person person) throws Exception;

}
