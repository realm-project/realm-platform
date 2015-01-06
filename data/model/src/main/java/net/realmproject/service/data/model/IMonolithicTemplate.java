package net.realmproject.service.data.model;

import net.objectof.facet.Facet;
import net.objectof.model.impl.IKind;
import net.objectof.util.impl.velocity.ITemplate;
import net.objectof.util.impl.velocity.ITemplateContext;

import org.apache.velocity.context.Context;

public abstract class IMonolithicTemplate extends ITemplate<IKind<?>> implements Facet<IKind<?>> {

	private boolean processed = false;

	public IMonolithicTemplate(ITemplateContext aContext, String aTemplateName) {
	    super(aContext, aTemplateName);
    }

	
	@Override
	public final Void process(IKind<?> aKind) throws Exception {
		
		if (processed) return null;
		super.process(aKind);
		processed = true;
		return null;
		
	}

	@Override
	protected Context defineContext(IKind<?> aResource) {
		Context context = super.defineContext(aResource);
		context.put("p", aResource.getPackage());
		return context;
	}

}
