package net.objectof.util;

public interface Loggable
{
  public enum Level
  {
    DEBUG, INFO, INIT, WARN, FATAL
  }

  public Level Debug = Level.DEBUG;
  public Level Info = Level.INFO;
  public Level Init = Level.INIT;
  public Level Warn = Level.WARN;
  public Level Fatal = Level.FATAL;

  public boolean isEnabled(Level aLevel);

  public void log(Level aLevel, Object aObject);
}
