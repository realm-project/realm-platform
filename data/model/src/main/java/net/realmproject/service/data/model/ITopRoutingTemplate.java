package net.realmproject.service.data.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.Context;

import net.objectof.Selector;
import net.objectof.model.impl.IKind;
import net.objectof.rt.impl.base.Util;
import net.objectof.util.impl.velocity.ITemplateContext;

public class ITopRoutingTemplate extends IMonolithicTemplate {

	public ITopRoutingTemplate(ITemplateContext aContext) {
	    super(aContext, "top-routing.vm");
    }

	@Override
	protected Context defineContext(IKind<?> aResource) {
		Context context = super.defineContext(aResource);
		context.put("util", Util.class);
		context.put("selector", Selector.class.getName());
		context.put("resttemplate", IRestRoutingTemplate.class);
		return context;
	}

	@Override
	protected Writer defineWriter(IKind<?> aKind) throws IOException {
		String packagePath = aKind.getPackage().getComponentName().replace('.', '/');
		File dir = new File(getContext().get("/").toString() + '/' + packagePath);
		dir.mkdirs();
		String fileName = "router.xml";
		File f = new File(dir, fileName);
		return new FileWriter(f);
	}
	
}
