package net.objectof.model.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.json.JsonValue;

import net.objectof.aggr.Composite;
import net.objectof.model.Locator;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.aggr.IComposite;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.impl.aggr.IMapped;
import net.objectof.rt.impl.IType;

public abstract class IDatatype<T> extends IType
{
  public static void toJSONString(String aString, Appendable aWriter)
      throws IOException
  {
    /*
     * Code within this method is adapted from org.json.JSONWriter. The
     * following license agreement applies to code within this method only:
     *
     * Copyright (c) 2006 JSON.org
     *
     * Permission is hereby granted, free of charge, to any person obtaining a
     * copy of this software and associated documentation files (the
     * "Software"), to deal in the Software without restriction, including
     * without limitation the rights to use, copy, modify, merge, publish,
     * distribute, sublicense, and/or sell copies of the Software, and to permit
     * persons to whom the Software is furnished to do so, subject to the
     * following conditions:
     */
    if (aString == null || aString.length() == 0)
    {
      aWriter.append("\"\"");
    }
    char b;
    char c = 0;
    String hhhh;
    int i;
    int len = aString.length();
    aWriter.append('"');
    for (i = 0; i < len; i += 1)
    {
      b = c;
      c = aString.charAt(i);
      switch (c)
      {
      case '\\':
      case '"':
        aWriter.append('\\');
        aWriter.append(c);
        break;
      case '/':
        if (b == '<')
        {
          aWriter.append('\\');
        }
        aWriter.append(c);
        break;
      case '\b':
        aWriter.append("\\b");
        break;
      case '\t':
        aWriter.append("\\t");
        break;
      case '\n':
        aWriter.append("\\n");
        break;
      case '\f':
        aWriter.append("\\f");
        break;
      case '\r':
        aWriter.append("\\r");
        break;
      default:
        if (c < ' ' || c >= '\u0080' && c < '\u00a0' || c >= '\u2000'
        && c < '\u2100')
        {
          aWriter.append("\\u");
          hhhh = Integer.toHexString(c);
          aWriter.append("0000", 0, 4 - hhhh.length());
          aWriter.append(hhhh);
        }
        else
        {
          aWriter.append(c);
        }
      }
    }
    aWriter.append('"');
  }

  private final IMetamodel theMetamodel;

  public IDatatype(IMetamodel aMetamodel)
  {
    theMetamodel = aMetamodel;
  }

  public abstract T fromJson(ITransaction aTx, JsonValue aObject);

  public final IMetamodel getMetamodel()
  {
    return theMetamodel;
  }

  public abstract Stereotype getStereotype();

  public String getTarget(String aRuntime, IKind<T> aKind)
  {
    // TODO need proper implementation.
    if (aRuntime.equals("java"))
    {
      return javaTarget(aKind);
    }
    throw new UnsupportedOperationException("Unsupported target.");
  }

  @Override
  public final String getUniqueName()
  {
    return getMetamodel().getUniqueName() + '/' + getStereotype();
  }

  public abstract boolean isAggregate();

  @SuppressWarnings("unchecked")
  public final T newInstance(ITransaction aTx, IKind<T> aKind, Object aLabel)
  {
    ITransaction tx = aTx;
    IId<T> id = new IBaseId<>(tx, aKind, aLabel);
    String impl = implClass(aKind);
    try
    {
    	try{
    		Constructor<T> cons = (Constructor<T>) Class.forName(impl)
    		.getConstructor(IId.class);
    		return cons.newInstance(id);
    	
    	//If we're working with datatypes which we do not have generated classes for
   		//(eg repo which is old, belongs to someone else, etc) we need to provide
    	//generic (superclass) implementations.
    	//TODO: This could become an issue if the concrete classes implement other interfaces that code expects,
    	} catch (ClassNotFoundException e2) {
    		
    		switch (aKind.getStereotype()) {
				case BOOL:
				case FN:
				case INT:
				case MEDIA:
				case MOMENT:
				case NUM:
				case REF:
				case SET:
				case TEXT:
				default:
					throw e2; //We really should be able to find the right classes for these ones
				
				case MAPPED:
					return (T) new IMapped<>(id);
					
				case INDEXED:
					return (T) new IIndexed<>(id);
					
				case COMPOSED:
					return (T) new IComposite((IId<Composite>) id);
    		}
    		
    		
    	}
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
    	
      throw new RuntimeException(e);
    }
  }

  public abstract void toJson(T aObject, Locator aLocator, Appendable aWriter)
      throws IOException;

  protected String implClass(IKind<?> aKind)
  {
    return peer().getName();
  }

  protected String javaTarget(IKind<T> aKind)
  {
    Class<?> cls = peer();
    if (cls.getPackage() == Package.getPackage("java.lang"))
    {
      return cls.getSimpleName();
    }
    else
    {
      return cls.getName();
    }
  }
}
