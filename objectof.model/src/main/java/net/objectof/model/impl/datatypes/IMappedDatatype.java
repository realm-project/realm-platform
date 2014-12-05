package net.objectof.model.impl.datatypes;

import java.io.IOException;
import java.util.Map.Entry;

import javax.json.JsonObject;
import javax.json.JsonValue;

import net.objectof.aggr.Mapping;
import net.objectof.model.Locator;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.impl.aggr.IMapped;

public class IMappedDatatype extends
    IResourceDatatype<Resource<Mapping<Object, Object>>>
{
  public IMappedDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.MAPPED;
  }

  @Override
  public boolean isAggregate()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return Mapping.class;
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return IMapped.class.getName();
  }

  @Override
  protected String javaTarget(IKind<Resource<Mapping<Object, Object>>> aKind)
  {
    IKind<?> t = aKind.getParts().get(0);
    return Mapping.class.getName() + "<String," + t.getTarget("java") + '>';
  }

  @Override
  protected void valueFromJson(ITransaction aTx,
      Resource<Mapping<Object, Object>> aResource, JsonValue aValue)
  {
	if (JsonValue.NULL.equals(aValue)) { return; }
    @SuppressWarnings("unchecked")
    IDatatype<Object> datatype = (IDatatype<Object>) ((IKind<?>) aResource.id()
        .kind()).getParts().get(0).datatype();
    Mapping<Object, Object> comp = aResource.value();
    JsonObject o = (JsonObject) aValue;
    for (Entry<String, JsonValue> e : o.entrySet())
    {
      String selector = e.getKey();
      JsonValue v = e.getValue();
      Object value = datatype.fromJson(aTx, v);
      comp.set(selector, value);
    }
  }

  @Override
  protected void valueToJson(Resource<Mapping<Object, Object>> aObject,
      Locator aLocator, Appendable aOut) throws IOException
  {
    @SuppressWarnings("unchecked")
    IDatatype<Object> datatype = (IDatatype<Object>) ((IKind<?>) aObject.id()
        .kind()).getParts().get(0).datatype();
    Mapping<Object, Object> comp = aObject.value();
    String delim = "";
    aOut.append('{');
    for (Object key : comp.keySet())
    {
      aOut.append(delim);
      delim = ",";
      Object value = comp.get(key);
      toJSONString(key.toString(), aOut);
      aOut.append(':');
      datatype.toJson(value, aLocator, aOut);
    }
    aOut.append('}');
  }
}
