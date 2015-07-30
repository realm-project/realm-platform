package net.realmproject.platform.security.authorization;


import java.io.IOException;

import javax.servlet.ServletException;

import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.security.authorization.authorizers.AbstractAuthorizer;
import net.realmproject.platform.util.RealmCorc;


public abstract class RealmAuthorizers extends AbstractAuthorizer {

    public RealmAuthorizers() {}

    protected static String method(HttpRequest request) {
        return request.getHttpRequest().getMethod();
    }

    protected static String className(Action action, HttpRequest request) {
        return RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());
    }

    protected static String recordName(Action action, HttpRequest request) {

        String method = request.getHttpRequest().getMethod();

        try {
            String className = RealmCorc.getNextPathElement(action.getName(), request.getHttpRequest());

            String recordName = null;
            switch (method) {
                case "POST":
                    IRepoAuthorizerJson data = RealmCorc.getJsonAsData(request.getReader(), IRepoAuthorizerJson.class);
                    recordName = data.loc;
                    break;
                case "GET":
                    recordName = RealmCorc.getNextPathElement(action.getName() + "/" + className,
                            request.getHttpRequest());
                    break;
                default:
                    break;
            }

            return recordName;
        }
        catch (NullPointerException | IOException | ServletException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected static String kind(Action action, HttpRequest request) {
        String record = recordName(action, request);
        if (isCreate(action, request)) return record;
        return record.split("-")[0];
    }

    protected static String label(Action action, HttpRequest request) {
        String record = recordName(action, request);
        if (isCreate(action, request)) return null;
        String[] parts = record.split("-");
        if (parts.length < 2) return null;
        String label = parts[1];
        parts = label.split("\\.");
        return parts[0];
    }

    protected static boolean isRead(Action action, HttpRequest request) {
        return request.getHttpRequest().getMethod().equals("GET");
    }

    protected static boolean isDelete(Action action, HttpRequest request) {
        return request.getHttpRequest().getMethod().equals("DELETE");
    }

    protected static boolean isPost(Action action, HttpRequest request) {
        return request.getHttpRequest().getMethod().equals("POST");
    }

    protected static boolean isUpdate(Action action, HttpRequest request) {
        if (!isPost(action, request)) return false;
        String recordName = recordName(action, request);
        if (recordName.contains("-")) return true;
        return false;
    }

    protected static boolean isCreate(Action action, HttpRequest request) {
        if (!isPost(action, request)) return false;
        String recordName = recordName(action, request);
        if (recordName.contains("-")) return false;
        return true;
    }

}
