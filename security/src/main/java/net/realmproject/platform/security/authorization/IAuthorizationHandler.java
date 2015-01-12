package net.realmproject.platform.security.authorization;


import net.objectof.corc.Action;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.corc.DatabaseRepository;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.ISessionHandler;


public class IAuthorizationHandler extends ISessionHandler {

    private Authorizer auth;

    public IAuthorizationHandler(Handler<?> aDefault, DatabaseRepository dbrepo, Authorizer auth) {
        super(aDefault, dbrepo);
        this.auth = auth;
    }

    @Override
    protected void onExecute(Action action, HttpRequest request, Person person) throws Exception {

        if (auth.authorize(action, request, person)) {
            chain(action, request);
        } else {
            request.getHttpResponse().sendError(403);
        }
    }

}
