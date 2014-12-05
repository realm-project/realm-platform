package net.objectof.model.impl.datatypes;

import java.io.IOException;
import java.util.Date;

import javax.json.JsonString;
import javax.json.JsonValue;

import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IDatatype;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IMetamodel;
import net.objectof.model.impl.IMoment;
import net.objectof.model.impl.ITransaction;

public class IMomentDatatype extends IDatatype<Date>
{
  public IMomentDatatype(IMetamodel aMetamodel)
  {
    super(aMetamodel);
  }

  @Override
  public Date fromJson(ITransaction aTx, JsonValue aObject)
  {
	if (aObject == null || JsonValue.NULL.equals(aObject)) { return null; }
    String src = ((JsonString) aObject).getString();
    return new IMoment(src);
  }

  @Override
  public Stereotype getStereotype()
  {
    return Stereotype.MOMENT;
  }

  @Override
  public boolean isAggregate()
  {
    return false;
  }

  @Override
  public Class<?> peer()
  {
    return Date.class;
  }

  @Override
  public void toJson(Date aObject, Locator aLocator, Appendable aWriter)
      throws IOException
  {
    if (aObject == null)
    {
      aWriter.append("null");
      return;
    }
    if (aObject instanceof IMoment == false)
    {
      aObject = new IMoment(aObject.getTime());
    }
    toJSONString(aObject.toString(), aWriter);
  }

  @Override
  protected String implClass(IKind<?> aKind)
  {
    return IMoment.class.getName();
  }
}
