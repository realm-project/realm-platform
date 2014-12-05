package net.objectof.ext;

/**
 * A Notice captures processing notices.
 * 
 * @author jdh
 * 
 */
public interface Notice
{
  public enum Severity
  {
    ERROR, WARNING, NOTIFICATION
  }

  public Object getLocation();

  public String getName();

  public Severity getSeverity();

  public String getDescription();
}
