package net.realmproject.platform.util;


import java.io.IOException;

import net.objectof.corc.web.v2.HttpRequest;


public class RealmError {

    public boolean display = false;
    public String message = "";

    public RealmError() {}

    public RealmError(String message) {
        this.message = message;
    }

    public RealmError(String message, boolean display) {
        this.message = message;
        this.display = display;
    }

    public String toString() {
        return RealmSerialize.serialize(this);
    }

    public static void send(HttpRequest request, String message) throws IOException {
        send(request, 400, message);
    }

    public static void send(HttpRequest request, int status, String message) throws IOException {
        send(request, status, message, false);
    }

    public static void send(HttpRequest request, int status, String message, boolean display) throws IOException {
        request.getHttpResponse().getWriter().print(new RealmError(message, display));
        request.getHttpResponse().setStatus(status);
    }

}
