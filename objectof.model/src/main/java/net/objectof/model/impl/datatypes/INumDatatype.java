package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonNumber;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class INumDatatype extends IDatatype<Double>
{
  public INumDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Double fromJson(ITransaction aTx, JsonValue aObject)
  {
    if (aObject == null || JsonValue.NULL.equals(aObject)) { return 0.0D; }
    return ((JsonNumber) aObject).doubleValue();
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.NUM;
  }

  @Override
  public boolean isAggregate()
  {
    return false;
  }

  @Override
  public Class<?> peer()
  {
    return Double.class;
  }

  @Override
  public void toJson(Double aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    aWriter.append(aObject == null ? "0.0" : aObject.toString());
  }
}
