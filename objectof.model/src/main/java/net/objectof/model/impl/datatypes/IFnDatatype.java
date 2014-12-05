package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonString;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class IFnDatatype extends IDatatype<String>
{
  public IFnDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public String fromJson(ITransaction aTx, JsonValue aObject)
  {
	if (aObject == null || JsonValue.NULL.equals(aObject)) { return null; }
    return ((JsonString) aObject).getString();
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.FN;
  }

  @Override
  public boolean isAggregate()
  {
    return false;
  }

  @Override
  public Class<?> peer()
  {
    return String.class;
  }

  @Override
  public void toJson(String aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    toJSONString(aObject, aWriter);
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return String.class.getName();
  }
}
