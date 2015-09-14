package net.realmproject.platform.api.utils;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.objectof.aggr.Composite;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.Id;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IPackage;
import net.objectof.model.query.IQuery;
import net.realmproject.dcm.util.DCMSerialize;
import net.realmproject.platform.api.datatypes.CreateSession;
import net.realmproject.platform.schema.Assignment;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.schema.Session;
import net.realmproject.platform.schema.Station;
import net.realmproject.platform.util.RealmCorc;
import net.realmproject.platform.util.model.Admins;
import net.realmproject.platform.util.model.Stations;
import net.realmproject.platform.util.model.Tokens;


public class APIUtils {

    static final long ONE_MINUTE_IN_MILLIS = 60000; // Milliseconds
    private Log log = LogFactory.getLog(getClass());

    public static String getLabel(String kindLabel) {

        String[] parts = null;

        try {
            parts = kindLabel.split("-");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (parts.length == 2) return parts[1];
        else return null;
    }

    public static String getStringFromRequest(String key, HttpRequest request) {

        String params;
        String result = null;
        JsonReader jsonReader = null;

        try {
            params = RealmCorc.getJson(request.getReader());
            jsonReader = Json.createReader(new StringReader(params));
            JsonObject jsonObject = jsonReader.readObject();
            String paramName = key.toLowerCase();
            result = jsonObject.getString(paramName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (jsonReader != null) jsonReader.close();
        }

        return result;

    }

    public static Object getObjectFromRequest(String kind, Transaction t, HttpRequest request) {

        Object o = null;
        String label = null;

        String paramValue = getStringFromRequest(kind, request);

        if (paramValue != null) {
            label = getLabel(paramValue);

            if (label != null) o = t.retrieve(kind, label);
        }

        return o;
    }

    // @SuppressWarnings("unchecked")
    public static void addStringResultToResponse(String result, HttpRequest request) {
        PrintWriter w = null;
        HttpServletResponse response = request.getHttpResponse();

        try {
            w = response.getWriter();
            w.write(result);
            w.flush();
        }
        catch (IOException e) {
//            e.printStackTrace();
        	System.out.println(e.toString());
        }
    }

    public static void addQueryResultToResponse(Iterable<?> queryResult, HttpRequest request) {
        String kind = null;
        Long lable = null;
        Resource<?> r;
        PrintWriter w = null;
        HttpServletResponse response = request.getHttpResponse();

        try {
            w = response.getWriter();

            for (Object o : queryResult) {
                r = (Resource<?>) o;
                kind = r.id().kind().getComponentName();
                lable = (Long) r.id().label();
                w.write(kind + "-" + lable);
                // r.send("application/json", w);
                w.write(',');
            }

            w.flush();
        }
        catch (IOException e) {
//            e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }
    
    public static void addObjectQueryResultToResponse(Transaction tx, Iterable<?> queryResult, HttpRequest request) {
        PrintWriter w = null;
        HttpServletResponse response = request.getHttpResponse();
        StringBuilder sb = new StringBuilder();

        try {
        	w = response.getWriter();
        	sb.append('[');
        	for (Object o : queryResult) {
        		@SuppressWarnings("unchecked")
				Resource<Object> r = (Resource<Object>) o;
        		Id<Object> id = r.id();
        		id.kind();
                IKind<Object> kind = (IKind<Object>) r.id().kind();
           		kind.datatype().toJson(r.value(), (IPackage) tx.getPackage(), sb);
           		sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1).append(']');

            w.write(sb.toString());
            w.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addObjectsToResponse(Iterable<?> queryResult, HttpRequest request) {
    	
    	PrintWriter w = null;
    	HttpServletResponse response = request.getHttpResponse();
    	
    	try {
    		w = response.getWriter();
    		
    		w.write(DCMSerialize.serialize(queryResult));
            w.write(',');

            w.flush();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    }

    public static int createSession(Transaction tx, Assignment assignment, Date date, Station station,
            CreateSession request, String type) {
    	int numberOfCreatedSessions = 0;
    	
        try {
            if (type.equals("single")) {

                Date startTime = concatDateTime(date, request.time.single.start);

                createSingleSession(tx, assignment, startTime, request.time.single.duration, station);
                numberOfCreatedSessions ++;
            } else if (type.equals("bulk")) {

                long gapBetweenSessions = 10 * ONE_MINUTE_IN_MILLIS;
                Date startTime = concatDateTime(date, request.time.bulk.start);
                Date endTime = concatDateTime(date, request.time.bulk.end);
                long startTimeInMilli = startTime.getTime();
                long endTimeInMilli = endTime.getTime();
                long durationInMilli = request.time.bulk.duration * ONE_MINUTE_IN_MILLIS;
                long currnetTime = startTimeInMilli;

                while (currnetTime + durationInMilli <= endTimeInMilli) {
                    createSingleSession(tx, assignment, new Date(currnetTime), request.time.bulk.duration, station);
                    numberOfCreatedSessions ++;
                    currnetTime += durationInMilli + gapBetweenSessions;

                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
		return numberOfCreatedSessions;
    }

    public static Date concatDateTime(Date date, String time) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((time.substring(0, 2))));
        calendar.set(Calendar.MINUTE, Integer.parseInt((time.substring(3, 5))));
        return calendar.getTime();
    }

    public static void createSingleSession(Transaction tx, Assignment assignment, Date startTime, long duration,
            Station station) {

        Session session = tx.create("Session");
        session.setAssignment(assignment);
        session.setStartTime(startTime);
        session.setDuration(duration);
        session.setSessionToken(Tokens.create());
        session.setStation(station);
        session.setCommands(tx.create("Session.commands"));
    }

    public static String convertDateToString(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static List<Date> createDateList(String stringStartDate, String stringEndDate) {
        List<Date> dates = new ArrayList<Date>();

        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringStartDate);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringEndDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().compareTo(endDate) <= 0) {
                dates.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    public static boolean dateIsInDays(Date date, String[] days) {

        List<String> daysList = new ArrayList<String>(Arrays.asList(days));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                if (daysList.contains("Sunday")) { return true; }
                break;
            case Calendar.MONDAY:
                if (daysList.contains("Monday")) { return true; }
                break;
            case Calendar.TUESDAY:
                if (daysList.contains("Tuesday")) { return true; }
                break;
            case Calendar.WEDNESDAY:
                if (daysList.contains("Wednesday")) { return true; }
                break;
            case Calendar.THURSDAY:
                if (daysList.contains("Thursday")) { return true; }
                break;
            case Calendar.FRIDAY:
                if (daysList.contains("Friday")) { return true; }
                break;
            case Calendar.SATURDAY:
                if (daysList.contains("Sathurday")) { return true; }
                break;
        }

        return false;
    }

    public static Resource<?> getResourceByName(Transaction tx, String kind, String resourceName) {
        Resource<?> resource = null;

        Iterable<Resource<?>> resources = tx.query(kind, new IQuery("name", resourceName));

        if (resources.iterator().hasNext()) {
            resource = resources.iterator().next();
        }

        return resource;
    }

    public static void nullSession(Transaction tx, String sessionLabel) {
        Session session = tx.retrieve("Session", sessionLabel);
        session.setAssignment(null);
        session.setCommands(null);
        session.setStation(null);
        session.setDuration(null);
        session.setSessionToken(null);
        session.setStartTime(null);
    }
    
    public static boolean hasReadAccessToSession(Person person, Session session) {

    	boolean isUser = person.getSessions().contains(session);

    	if (isUser || hasWriteAccessToSession(person, session))
    		return true;
    	
    	return false;
    }
    
    public static boolean hasWriteAccessToSession(Person person, Session session) {

    	Station station = session.getStation();
    	
    	boolean isStationOwner = person.equals(station.getOwner());
    	boolean isStationSharer = Stations.isSharer(person, station);
    	boolean isAdmin = Admins.isAdmin(person);

    	if (isStationOwner || isStationSharer || isAdmin)
    		return true;
    	
    	return false;
    }
}
