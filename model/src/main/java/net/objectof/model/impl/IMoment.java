package net.objectof.model.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * A Moment extends java.util.Date to provide a canonical UTC Date with second
 * precision: milliseconds are truncated. A Moment is canonically represented as
 * a String in ISO 8601 compatible format. Moments use the following convention
 * when converting to and from a String:
 * <ul>
 * <li>when the time-of-day is 00:00:00 UTC, the moment is expressed as
 * yyyy-MM-dd.
 * <li>otherwise, the moment is expressed as yyyy-MM-ddTHH:mm:ssZ.
 * </ul>
 * Both forms are supported when converting from a String. This 'dual precision'
 * approach provides a reasonable default for converting between dates,
 * datetimes, and strings thereof. (Thought:) The ambiguous nature of
 * "00:00:00Z" *could* be resolved by the prior date with a value of
 * "24:00:00Z", i.e. a way of representing the given date with explicit
 * precision. This is currently NOT supported.
 * <p>
 * Support is provided for working with time zones, however this support has not
 * yet been extensively tested. It is assumed a Calendar will be used to
 * represent the moment in a given calendar or localized format.
 * 
 * @author jdh
 */
public class IMoment extends java.util.Date
{
  private final static class Fmt extends SimpleDateFormat
  {
    private static final long serialVersionUID = 1L;

    public Fmt(String aFormat)
    {
      super(aFormat);
    }

    public long getTime(String aDate) throws ParseException
    {
      return super.parse(aDate).getTime();
    }

    @Override
    public IMoment parse(String aDate) throws ParseException
    {
      return new IMoment(IMoment.getTime(aDate, getTimeZone()));
    }
  }

  private static final long serialVersionUID = 1L;
  public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String DATE_FORMAT = "yyyy'-'MM'-'dd";
  private static final Fmt FMT = new Fmt(DATETIME_FORMAT);
  private static final Fmt DATE_FMT = new Fmt(DATE_FORMAT);
  private static final String START_OF_DAY = "T00:00:00Z";

  public static long getTime(long aTime, TimeZone aTimeZone)
  {
    if (aTimeZone == null)
    {
      aTimeZone = TimeZone.getDefault();
    }
    return aTime + aTimeZone.getOffset(aTime);
  }

  public static long getTime(String aDate) throws IllegalArgumentException
  {
    try
    {
      return aDate.length() == 10 ? DATE_FMT.getTime(aDate) : FMT
          .getTime(aDate);
    }
    catch (ParseException e)
    {
      throw new IllegalArgumentException(e);
    }
  }

  public static long getTime(String aDate, TimeZone aTimeZone) throws IllegalArgumentException
  {
    long time = getTime(aDate);
    if (aTimeZone == null)
    {
      aTimeZone = TimeZone.getDefault();
    }
    return time + aTimeZone.getOffset(time);
  }

  public IMoment()
  {
    this(System.currentTimeMillis(), TimeZone.getDefault());
  }

  public IMoment(long aTimestamp)
  {
    super(aTimestamp / 1000 * 1000);
  }

  public IMoment(long aTimeStamp, TimeZone aTimeZone)
  {
    // Because we're converting to UTC, we need to negate the offset:
    this(aTimeStamp - aTimeZone.getOffset(aTimeStamp));
  }

  public IMoment(String aDate) throws IllegalArgumentException
  {
    setDate(aDate);
  }

  public IMoment(String aDate, TimeZone aTimeZone)
  {
    setDate(aDate, aTimeZone);
  }

  /**
   * Sets the Date from a canonical String format.
   * 
   * @param aDate
   *          The Date in either {@code DateFormat.TIME_FORMAT} or
   *          {@code DateFormat.DATE_FORMAT}
   * @throws ParseException
   */
  public void setDate(String aDate) throws IllegalArgumentException
  {
    setTime(getTime(aDate));
  }

  /**
   * Sets the Date from a canonical String format, converting from aTimeZone
   * into UTC.
   * 
   * @param aDate
   *          The Date in either {@code DateFormat.TIME_FORMAT} or
   *          {@code DateFormat.DATE_FORMAT}
   * @param aTimeZone
   *          The TimeZone in which aDateTime is representing. {@code null} may
   *          be used to represent the JVM's default TimeZone.
   * @throws IllegalArgumentException
   */
  public void setDate(String aDate, TimeZone aTimeZone)
  {
    setTime(getTime(aDate, aTimeZone));
  }

  public void setDate(String aDate, TimeZone aTimeZone, DateFormat aFormat)
      throws ParseException
  {
    long time = aFormat.parse(aDate).getTime();
    setTime(getTime(time, aTimeZone));
  }

  @Override
  public void setTime(long aTime)
  {
    /*
     * Dates hold to a second. Another (integer or long) value is required to
     * hold sub-second precision.
     */
    super.setTime(aTime / 1000L * 1000L);
  }

  @Override
  public String toString()
  {
    String out = FMT.format(this);
    if (out.contains(START_OF_DAY))
    {
      out = out.substring(0, out.length() - START_OF_DAY.length());
    }
    return out;
  }

  public String toString(TimeZone aTimeZone)
  {
    long time = getTime();
    if (aTimeZone == null)
    {
      aTimeZone = TimeZone.getDefault();
    }
    return new IMoment(time + aTimeZone.getOffset(time)).toString();
  }

  public String toString(TimeZone aTimeZone, DateFormat aFormat)
  {
    long time = getTime();
    if (aTimeZone == null)
    {
      aTimeZone = TimeZone.getDefault();
    }
    return aFormat.format(new IMoment(time + aTimeZone.getOffset(time)));
  }
}
