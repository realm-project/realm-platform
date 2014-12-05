package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonArray;
import javax.json.JsonValue;

import net.objectof.aggr.Set;
import net.objectof.aggr.impl.ISet;
import net.objectof.model.Locator;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class ISetDatatype extends IResourceDatatype<Resource<Set<Object>>>
{
  public ISetDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.SET;
  }

  @Override
  public boolean isAggregate()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return Set.class;
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return ISet.class.getName();
  }

  @Override
  protected String javaTarget(IKind<Resource<Set<Object>>> aKind)
  {
    IKind<?> t = aKind.getParts().get(0);
    return Set.class.getName() + '<' + t.getTarget("java") + '>';
  }

  @Override
  protected void valueFromJson(ITransaction aTx,
      Resource<Set<Object>> aResource, JsonValue aValue)
  {
	if (JsonValue.NULL.equals(aValue)) { return; }
    @SuppressWarnings("unchecked")
    IDatatype<Object> datatype = (IDatatype<Object>) ((IKind<?>) aResource.id()
        .kind()).getParts().get(0).datatype();
    Set<Object> comp = aResource.value();
    JsonArray o = (JsonArray) aValue;
    for (JsonValue v : o)
    {
      Object value = datatype.fromJson(aTx, v);
      comp.add(value);
    }
  }

  @Override
  protected void valueToJson(Resource<Set<Object>> aObject, Locator aLocator,
      Appendable aOut) throws IOException
  {
    @SuppressWarnings("unchecked")
    IDatatype<Object> datatype = (IDatatype<Object>) ((IKind<?>) aObject.id()
        .kind()).getParts().get(0).datatype();
    Set<Object> comp = aObject.value();
    String delim = "";
    aOut.append('[');
    for (Object value : comp)
    {
      aOut.append(delim);
      delim = ",";
      datatype.toJson(value, aLocator, aOut);
    }
    aOut.append(']');
  }
}
