package net.realmproject.platform.schema.tests;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.objectof.model.Resource;
import net.objectof.model.ResourceException;
import net.objectof.model.Transaction;
import net.objectof.model.impl.IKind;
import net.objectof.model.impl.IPackage;

public class PrintObject
{
  public static void print(Transaction aTx, Object aObject)
  {
    @SuppressWarnings("unchecked")
    Resource<Object> o = (Resource<Object>) aObject;
    IKind<Object> kind = (IKind<Object>) o.id().kind();
    Writer w = new OutputStreamWriter(System.out);
    try {
		kind.datatype().toJson(o.value(), (IPackage) aTx.getPackage(), w);
		w.write('\n');
	    w.flush();
	} catch (ResourceException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
  }
}
