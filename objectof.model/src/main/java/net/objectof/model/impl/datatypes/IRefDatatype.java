package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonObject;
import javax.json.JsonValue;

import net.objectof.facet.Property;
import net.objectof.model.Locator;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.IProperties;
import net.objectof.model.impl.ITransaction;
import net.objectof.rt.impl.base.Util;

public class IRefDatatype extends IDatatype<Resource<?>>
{
  public IRefDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Resource<?> fromJson(ITransaction aTx, JsonValue aObject)
  {
	if (JsonValue.NULL.equals(aObject)) { return null; }
    String loc = ((JsonObject) aObject).getString("loc");
    int separator = loc.indexOf('-');
    if (separator < 0)
    {
      throw new RuntimeException("Invalid Reference.");
    }
    String componentName = loc.substring(0, separator);
    String label = loc.substring(++separator);
    Resource<?> res = (Resource<?>) aTx.retrieve(componentName, label);
    return res;
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.REF;
  }

  @Override
  public boolean isAggregate()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return Resource.class;
  }

  @Override
  public void toJson(Resource<?> aObject, Locator aLocator, Appendable aOut)
      throws IOException
  {
    if (aObject == null)
    {
      aOut.append("null");
      return;
    }
    aOut.append("{\"loc\":");
    toJSONString(aLocator.locate(aObject.id()), aOut);
    aOut.append('}');
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return Resource.class.getName();
  }

  @Override
  protected String javaTarget(IKind<Resource<?>> aKind)
  {
    Property to = aKind.getProperty(IProperties.HREF);
    if (to == null)
    {
      return Util.initCap(aKind.getSelector());
    }
    return to.getSource();
  }
}
