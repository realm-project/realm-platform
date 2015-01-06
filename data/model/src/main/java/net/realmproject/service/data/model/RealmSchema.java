package net.realmproject.service.data.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;

public class RealmSchema {

	private static File schemafile;
	
	public static File get() {
		try {
			
			if (schemafile == null) {
				InputStream stream = RealmSchema.class.getResourceAsStream("/packages/realm.xml");
				schemafile = File.createTempFile("realm-schema-", ".xml");
				Writer writer = new FileWriter(schemafile);
				writer.write(IOUtils.toString(stream));
				writer.close();
			}
			
			return schemafile;
			
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
