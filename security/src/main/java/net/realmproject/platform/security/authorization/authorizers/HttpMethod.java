package net.realmproject.platform.security.authorization.authorizers;


import java.util.Collections;
import java.util.List;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;


public class HttpMethod extends AbstractAuthorizer {

    private List<String> methods;

    public HttpMethod(String method) {
        this(Collections.singletonList(method));
    }

    public HttpMethod(List<String> methods) {
        this.methods = methods;
    }

    @Override
    public boolean authorize(Action action, HttpRequest request, Person person) {

        String httpMethod = request.getHttpRequest().getMethod();

        if (methods.contains(httpMethod)) return true;
        return false;

    }

}
