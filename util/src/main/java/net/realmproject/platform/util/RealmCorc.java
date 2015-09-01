package net.realmproject.platform.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Transaction;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.util.model.Persons;


public class RealmCorc {

    public static List<String> getPath(HttpServletRequest http) {
        String fullUrl = http.getRequestURI().trim();
        return getPath(fullUrl);
    }

    public static List<String> getPath(String fullUrl) {

        if (fullUrl.length() == 0) return new Stack<>();
        if (fullUrl.charAt(0) == '/') fullUrl = fullUrl.substring(1);
        if (fullUrl.length() == 0) return new Stack<>();

        return new ArrayList<>(Arrays.asList(fullUrl.split("/+")));

    }

    public static String getNextPathElement(String current, HttpServletRequest http) {
        return getNextPathElement(current, http.getRequestURI());
    }

    public static String getNextPathElement(String current, String full) {

        List<String> fullPath = RealmCorc.getPath(full);
        List<String> currentPath = RealmCorc.getPath(current);

        if (fullPath.size() <= currentPath.size()) return null;
        return fullPath.get(currentPath.size());
    }

    public static String getRemainingPath(String current, HttpServletRequest http) {
        return getRemainingPath(current, http.getRequestURI());
    }

    public static String getRemainingPath(String current, String full) {

        List<String> fullPath = RealmCorc.getPath(full);
        fullPath.remove(0); // context root
        List<String> currentPath = RealmCorc.getPath(current);

        if (fullPath.size() <= currentPath.size()) return null;
        while (currentPath.size() > 0) {
            currentPath.remove(0);
            fullPath.remove(0);
        }
        return StringUtils.join(fullPath, "/");
    }

    public static String getJson(Reader jsonReader) throws IOException {

        if (jsonReader.markSupported()) {
            // jsonReader.mark(Integer.MAX_VALUE);
            jsonReader.mark(32 * 1024 * 1024);
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(jsonReader);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonReader.markSupported()) {
            jsonReader.reset();
        }

        return sb.toString();

    }

    public static <T> T getJsonAsData(Reader reader, Class<T> c) throws IOException, ServletException {
        String json = getJson(reader);
        return DCMSerialize.deserialize(json, c);
    }

    public static Person getUser(Transaction tx, HttpRequest request) {

        Object oUsername = getUsername(request);
        if (oUsername == null) { return null; }

        try {
            String username = (String) oUsername;
            return Persons.fromUsername(tx, username);
        }
        catch (ClassCastException e) {
            return null;
        }

    }

    public static String getUsername(HttpRequest request) {

        HttpSession session = request.getHttpRequest().getSession(false);
        if (session == null) { return null; }
        return session.getAttribute("person").toString();

    }

}
