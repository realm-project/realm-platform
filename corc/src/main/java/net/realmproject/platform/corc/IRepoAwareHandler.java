package net.realmproject.platform.corc;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.objectof.model.Package;


public class IRepoAwareHandler extends IHandler<HttpRequest> implements RepoAware {

    private final Package repo;

    public IRepoAwareHandler(Connector connector) throws ConnectorException {
        repo = connector.getPackage();
    }

    public IRepoAwareHandler(Handler<?> aDefault, Connector connector) throws ConnectorException {
        super(aDefault);
        repo = connector.getPackage();
    }

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    @Override
    public Package repo() {
        return repo;
    }

}
