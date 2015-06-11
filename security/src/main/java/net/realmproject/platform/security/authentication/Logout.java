package net.realmproject.platform.security.authentication;


import java.io.IOException;

import javax.servlet.http.HttpSession;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.impl.corc.IHandler;
import net.realmproject.platform.util.RealmResponse;


public class Logout extends IHandler<HttpRequest> {

    @Override
    public Class<? extends HttpRequest> getArgumentClass() {
        return HttpRequest.class;
    }

    protected void onExecute(Action action, HttpRequest request) throws IOException {
        HttpSession session = request.getHttpRequest().getSession(false);
        if (session == null) {
            RealmResponse.send(request, 401, "You have already logged out");
            return;
        }
        session.invalidate();
        RealmResponse.send(request, 200, "Logged Out");
    }
}
