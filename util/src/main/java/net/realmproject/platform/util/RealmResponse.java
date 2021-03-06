package net.realmproject.platform.util;


import java.io.IOException;

import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.dcm.util.DCMSerialize;


public class RealmResponse {

    public boolean display = false;
    public String message = "";

    public RealmResponse() {}

    public RealmResponse(String message) {
        this.message = message;
    }

    public RealmResponse(String message, boolean display) {
        this.message = message;
        this.display = display;
    }

    public String toString() {
        return DCMSerialize.serialize(this);
    }

    public static void sendJson(HttpRequest request, Object toJson) throws IOException {
        send(request, 200, DCMSerialize.serialize(toJson));
    }

    public static void sendOk(HttpRequest request, String message) throws IOException {
        send(request, 200, message);
    }

    public static void sendError(HttpRequest request, String message) throws IOException {
        send(request, 400, message);
    }

    public static void send(HttpRequest request, int status, String message) throws IOException {
        send(request, status, message, false);
    }

    public static void send(HttpRequest request, int status, String message, boolean display) throws IOException {
        request.getHttpResponse().getWriter().print(new RealmResponse(message, display));
        request.getHttpResponse().setStatus(status);
        request.getHttpResponse().getWriter().close();
    }

}
