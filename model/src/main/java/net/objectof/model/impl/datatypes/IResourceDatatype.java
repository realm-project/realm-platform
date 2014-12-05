package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonObject;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IId;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public abstract class IResourceDatatype<T extends Resource<?>> extends
    IDatatype<T>
{
  public IResourceDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public T fromJson(ITransaction aTx, JsonValue aValue)
  {
	if (JsonValue.NULL.equals(aValue)) { return null; }
    JsonObject object = (JsonObject) aValue;
    String loc = object.getString("loc");
    IKind<T> kind = kindFrom(aTx.getPackage(), loc);
    Object label = labelFrom(kind, loc);
    T res = newInstance(aTx, kind, null);
    valueFromJson(aTx, res, object.get("value"));
    IId<?> id = (IId<?>) res.id();
    id.relabel(label);
    aTx.put(res.getUniqueName(), res);
    return res;
  }

  @Override
  public void toJson(T aObject, Locator aLocator, Appendable aOut)
      throws IOException
  {
    if (aObject == null)
    {
      aOut.append("null");
      return;
    }
    aOut.append("{\"loc\":");
    toJSONString(aLocator.locate(aObject.id()), aOut);
    aOut.append(",\"value\":");
    valueToJson(aObject, aLocator, aOut);
    aOut.append('}');
  }

  protected IKind<T> kindFrom(Package aPackage, String aLoc)
  {
    int separator = aLoc.indexOf('-');
    String componentName = separator < 0 ? aLoc : aLoc.substring(0, separator);
    @SuppressWarnings("unchecked")
    IKind<T> kind = (IKind<T>) aPackage.forName(componentName);
    return kind;
  }

  protected Object labelFrom(IKind<T> aKind, String aLoc)
  {
    int separator = aLoc.indexOf('-');
    if (separator > 0)
    {
      String label = aLoc.substring(++separator);
      return parseLabel(label);
    }
    return aKind.nextTransientLabel();
  }

  protected Object parseLabel(String aLabel)
  {
    return Long.parseLong(aLabel);
  }

  protected abstract void valueFromJson(ITransaction aTx, T aResource,
      JsonValue aValue);

  protected abstract void valueToJson(T aObject, Locator aLocator,
      Appendable aOut) throws IOException;
}
