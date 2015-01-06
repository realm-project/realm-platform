package net.realmproject.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.DateStart;
import biweekly.util.Duration;

public class RealmMedia {


	public static void email(String to, String subject, String text) {
		
		String from = "noreply@realmproject.net";
		String host = "localhost";
		
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		
		Session mailSession = Session.getDefaultInstance(properties);
		
		try {
			
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			msg.setSubject(subject);
			msg.setText(text);
			
			Transport.send(msg);
						
		} catch ( MessagingException e ) {
			e.printStackTrace();
		}
	}
	
	
	public static String iCal(String summary, Date start, int minutes) {
		
		ICalendar ical = new ICalendar();
		VEvent event = new VEvent();
		
		event.setSummary(summary).setLanguage("en-ca");
		event.setDateStart(new DateStart(start));
		event.setDuration(new Duration.Builder().minutes(minutes).build());
		
		ical.addEvent(event);
		return ical.write();
		
	}
	
}
