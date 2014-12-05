package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonNumber;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class IIntDatatype extends IDatatype<Long>
{
  public IIntDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Long fromJson(ITransaction aTx, JsonValue aObject)
  {
	if (aObject == null || JsonValue.NULL.equals(aObject)) { return 0L; }
    return ((JsonNumber) aObject).longValueExact();
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.INT;
  }

  @Override
  public boolean isAggregate()
  {
    return false;
  }

  @Override
  public Class<?> peer()
  {
    return Long.class;
  }

  @Override
  public void toJson(Long aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    aWriter.append(aObject == null ? "0" : aObject.toString());
  }
}
