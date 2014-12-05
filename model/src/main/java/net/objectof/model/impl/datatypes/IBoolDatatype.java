package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class IBoolDatatype extends IDatatype<Boolean>
{
  public IBoolDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Boolean fromJson(ITransaction aTx, JsonValue aObject)
  {
	if (aObject == null || JsonValue.NULL.equals(aObject)) { return Boolean.FALSE; }
    return aObject.getValueType() == ValueType.FALSE ? Boolean.FALSE : Boolean.TRUE;
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.BOOL;
  }

  @Override
  public boolean isAggregate()
  {
    return false;
  }

  @Override
  public Class<?> peer()
  {
    return Boolean.class;
  }

  @Override
  public void toJson(Boolean aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    aWriter.append(aObject == Boolean.TRUE ? "true" : "false");
  }
}
