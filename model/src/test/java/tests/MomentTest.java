package tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import net.objectof.model.impl.IMoment;

import org.junit.Test;

public class MomentTest
{
  public static void main(String[] a)
  {
    String timeZoneIds[] = TimeZone.getAvailableIDs();
    for (int i = 0; i < timeZoneIds.length; i++)
    {
      // TimeZone tz = TimeZone.getTimeZone(timeZoneIds[i]);
      System.out.print(timeZoneIds[i] + " ");
    }
    System.out.println();
  }

  @Test
  public void dateTest() throws ParseException
  {
    System.out.println(new IMoment("2014-05-01").toString());
  }
  @Test
  public void dateTest2() throws ParseException
  {
    System.out.println(new IMoment("2014-05-01T00:00:00Z").toString());
  }

  @Test
  public void localTime() throws ParseException
  {
    TimeZone tz = TimeZone.getTimeZone("Canada/Eastern");
    SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy h:mm a",
        Locale.CANADA_FRENCH);
    IMoment d = new IMoment("2010-05-18T18:25:00Z");
    System.out.println(d.toString() + " Canada/Eastern/French: "
        + d.toString(tz, fmt));
  }

  @Test
  public void test()
  {
    IMoment d = new IMoment();
    String str = d.toString();
    System.out.println(str);
  }

  @Test
  public void test2() throws ParseException
  {
    IMoment d = new IMoment("2010-02-18T18:25:00Z");
    System.out.println();
    System.out.println(d + " default " + d.toString(TimeZone.getDefault()));
    System.out.println(d + " eastern "
        + d.toString(TimeZone.getTimeZone("Canada/Eastern")));
    System.out.println(d + " Berlin "
        + d.toString(TimeZone.getTimeZone("Europe/Berlin")));
    System.out.println(d + " Guadalcanal "
        + d.toString(TimeZone.getTimeZone("Pacific/Guadalcanal")));
    System.out.println(d + " Honolulu "
        + d.toString(TimeZone.getTimeZone("Pacific/Honolulu")));
  }
}
