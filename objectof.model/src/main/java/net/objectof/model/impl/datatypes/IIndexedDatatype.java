package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonArray;
import javax.json.JsonValue;

import net.objectof.aggr.Listing;
import net.objectof.model.Locator;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;
import net.objectof.model.impl.aggr.IIndexed;

public class IIndexedDatatype extends
    IResourceDatatype<Resource<Listing<Object>>>
{
  public IIndexedDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.INDEXED;
  }

  @Override
  public boolean isAggregate()
  {
    return true;
  }

  @Override
  public Class<?> peer()
  {
    return Listing.class;
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return IIndexed.class.getName();
  }

  @Override
  protected String javaTarget(IKind<Resource<Listing<Object>>> aKind)
  {
    IKind<?> t = aKind.getParts().get(0);
    return Listing.class.getName() + '<' + t.getTarget("java") + '>';
  }

  @Override
  protected void valueFromJson(ITransaction aTx,
      Resource<Listing<Object>> aResource, JsonValue aValue)
  {
	if (JsonValue.NULL.equals(aValue)) { return; }
    Listing<Object> list = aResource.value();
    @SuppressWarnings("unchecked")
    IKind<Object> entryKind = (IKind<Object>) ((IKind<?>) aResource.id().kind())
        .getParts().get(0);
    JsonArray arr = (JsonArray) aValue;
    for (JsonValue v : arr)
    {
      Object value = entryKind.datatype().fromJson(aTx, v);
      list.add(value);
    }
  }

  @Override
  protected void valueToJson(Resource<Listing<Object>> aObject,
      Locator aLocator, Appendable aOut) throws IOException
  {
    IKind<?> kind = (IKind<?>) ((Resource<?>) aObject).id().kind();
    @SuppressWarnings("unchecked")
    IKind<Object> member = (IKind<Object>) kind.getParts().get(0);
    String delim = "";
    aOut.append("[");
    for (Object ele : aObject.value())
    {
      aOut.append(delim);
      delim = ",";
      member.datatype().toJson(ele, aLocator, aOut);
    }
    aOut.append("]");
  }
}
