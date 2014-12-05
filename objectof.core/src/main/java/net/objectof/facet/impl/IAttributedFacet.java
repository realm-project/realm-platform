package net.objectof.facet.impl;

import java.util.HashMap;
import java.util.Map;

import net.objectof.facet.Annotated;
import net.objectof.facet.Property;

public class IAttributedFacet<T extends Annotated> extends IFacet<T>
{
  private final Map<String, IAttribute<Object>> theAttributes;

  public IAttributedFacet(String aUniqueName)
  {
    super(aUniqueName);
    theAttributes = new HashMap<String, IAttribute<Object>>();
  }

  @SafeVarargs
  public IAttributedFacet(String aUniqueName, IAttribute<Object>... aAttributes)
  {
    this(aUniqueName);
    for (IAttribute<Object> attr : aAttributes)
    {
      addAttribute(attr);
    }
  }

  public IAttribute<Object> getAttribute(String aSelector)
  {
    return theAttributes.get(aSelector);
  }

  @Override
  public Object process(Annotated aObject, String aSelector)
  {
    Property property = aObject.getProperty(getUniqueName() + '/' + aSelector);
    if (property == null)
    {
      return null;
    }
    IAttribute<Object> attr = getAttribute(aSelector);
    if (attr == null)
    {
      return super.process(aObject, aSelector);
    }
    return attr.convert(property);
  }

  protected void addAttribute(IAttribute<Object> aAttribute)
  {
    String name = aAttribute.getName();
    if (theAttributes.containsKey(name))
    {
      throw new IllegalArgumentException("Attribute '" + aAttribute.getName()
          + "' already defined.");
    }
    theAttributes.put(name, aAttribute);
  }
}
