package net.objectof.ext.impl;

import java.util.Arrays;

import net.objectof.ext.Notice;

public class INotice implements Notice
{
  private final Object theLocation;
  private final Severity theSeverity;
  private final String theName;
  private final Object[] theArguments;

  public INotice(String aName, Object aLocation, Object... aArguments)
  {
    theName = aName;
    theLocation = aLocation;
    // TODO base the severity on a lookup of the Name.
    theSeverity = Severity.ERROR;
    theArguments = aArguments;
  }

  public Object[] getArguments()
  {
    return theArguments;
  }

  @Override
  public String getDescription()
  {
    return theName + ": " + Arrays.deepToString(theArguments);
  }

  @Override
  public Object getLocation()
  {
    return theLocation;
  }

  @Override
  public String getName()
  {
    return theName;
  }

  @Override
  public Severity getSeverity()
  {
    return theSeverity;
  }

  @Override
  public String toString()
  {
    StringBuilder b = new StringBuilder();
    b.append(getSeverity().toString().toUpperCase()).append(" at: ")
        .append(getLocation()).append(" - ").append(getDescription());
    return b.toString();
  }
}
