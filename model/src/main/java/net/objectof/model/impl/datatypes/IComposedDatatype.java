package net.objectof.model.impl.datatypes;

import java.io.IOException;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.json.JsonValue;

import net.objectof.aggr.Composite;
import net.objectof.model.Locator;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class IComposedDatatype extends IResourceDatatype<Resource<Composite>>
{
  public IComposedDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.COMPOSED;
  }

  @Override
  public boolean isAggregate()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return Composite.class;
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    String selector = aKind.getSelector();
    return aKind.getPackage().getComponentName() + ".composite.I"
        + Character.toUpperCase(selector.charAt(0)) + selector.substring(1)
        + "Bean";
  }

  @Override
  protected String javaTarget(IKind<Resource<Composite>> aKind)
  {
    String selector = aKind.getSelector();
    return aKind.getPackage().getComponentName() + '.'
        + Character.toUpperCase(selector.charAt(0)) + selector.substring(1);
  }

  @Override
  protected void valueFromJson(ITransaction aTx,
      Resource<Composite> aResource, JsonValue aValue)
  {
	if (JsonValue.NULL.equals(aValue)) { return; }
    IKind<?> kind = (IKind<?>) aResource.id().kind();
    Composite comp = aResource.value();
    JsonObject o = (JsonObject) aValue;
    for (Entry<String, JsonValue> e : o.entrySet())
    {
      String selector = e.getKey();
      JsonValue v = e.getValue();
      @SuppressWarnings("unchecked")
      IKind<Object> entryKind = (IKind<Object>) kind.findMember(selector);
      if (entryKind == null)
      {
        throw new RuntimeException("INVALID SELECTOR: " + selector);
      }
      Object value = entryKind.datatype().fromJson(aTx, v);
      comp.set(selector, value);
    }
  }

  @Override
  protected void valueToJson(Resource<Composite> aObject, Locator aLocator,
      Appendable aOut) throws IOException
  {
    Composite comp = aObject.value();
    String delim = "";
    aOut.append('{');
    for (IKind<?> member : ((IKind<?>) aObject.id().kind()).getParts())
    {
      aOut.append(delim);
      delim = ",";
      String key = member.getSelector();
      Object value = comp.get(key);
      toJSONString(key, aOut);
      aOut.append(':');
      @SuppressWarnings("unchecked")
      IDatatype<Object> datatype = (IDatatype<Object>) member.datatype();
      datatype.toJson(value, aLocator, aOut);
    }
    aOut.append('}');
  }
}
