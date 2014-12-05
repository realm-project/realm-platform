package net.objectof.model.impl.datatypes;

import java.io.IOException;

import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.ITransaction;

public class IUnsupportedDatatype extends IDatatype<Void>
{
  public IUnsupportedDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Void fromJson(ITransaction aTx, JsonValue aObject)
  {
    return null;
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
    return Void.class;
  }

  @Override
  public void toJson(Void aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    aWriter.append("null");
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return Void.class.getName();
  }
}
