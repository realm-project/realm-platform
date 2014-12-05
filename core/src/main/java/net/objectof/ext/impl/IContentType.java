package net.objectof.ext.impl;

import net.objectof.ext.ContentType;

public class IContentType implements ContentType
{
  public static final ContentType XML = new IContentType(Unit.CHARACTER,
      "text/xml", "toXml:");
  private final Unit theUnit;
  private final String theMediaType;
  private final String theCallbackSelector;

  private IContentType(Unit aUnit, String aMediaType, String aCallbackSelector)
  {
    theUnit = aUnit;
    theMediaType = aMediaType;
    theCallbackSelector = aCallbackSelector;
  }

  @Override
  public String getCallbackSelector()
  {
    return theCallbackSelector;
  }

  @Override
  public String getMediaType()
  {
    return theMediaType;
  }

  @Override
  public String getUniqueName()
  {
    // TODO Auto-generated stub
    throw new UnsupportedOperationException();
  }

  @Override
  public Unit getUnit()
  {
    return theUnit;
  }

  @Override
  public boolean isInstance(Object aObject)
  {
    // This is an abstract type.
    return false;
  }
}
