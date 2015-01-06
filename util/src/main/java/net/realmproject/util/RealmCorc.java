package net.realmproject.util;

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

import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.realmproject.model.schema.Person;
import net.realmproject.util.model.Persons;

import org.apache.commons.lang.StringUtils;


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
	
	public static String getJson(Reader jsonReader) throws IOException, ServletException {

		if (jsonReader.markSupported()) { jsonReader.mark(Integer.MAX_VALUE); }
		
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(jsonReader);
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (jsonReader.markSupported()) { jsonReader.reset(); }
		
		return sb.toString();

	}
	
	public static <T> T getJsonAsData(Reader reader, Class<T> c) throws IOException, ServletException {
		String json = getJson(reader);
		return RealmSerialize.deserialize(json, c);
	}
	
	public static Person getUser(Transaction tx, HttpRequest request) {
		
		HttpSession session = request.getHttpRequest().getSession(false);
		if (session == null) { return null; }
		
		Object oUsername = session.getAttribute("person");
		if (oUsername == null) { return null; }
		
		try {
			String username = (String) oUsername;
			return Persons.fromUsername(tx, username);
		} catch (ClassCastException e) {
			return null;
		}
		
	}
	
	
}
