package net.objectof.model.query;

import java.util.HashSet;
import java.util.Set;

import net.objectof.aggr.Aggregate;
import net.objectof.model.Id;
import net.objectof.model.Package;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IComposite;

public class ILoadQuery implements Query {

	private Id<?> other;
	private String otherField;
	
	public ILoadQuery(Id<?> other, String otherField) {
		this.other = other;
		this.otherField = otherField;
	}
	
	@Override
	public Set<String> resolve(int aLimit, Package aPackage, String aKind, QueryResolver resolver) {
		
		Transaction tx = aPackage.connect(getClass().getName());
		IComposite otherComp = tx.retrieve(other);
		Aggregate<?, Resource<?>> values = (Aggregate<?, Resource<?>>) otherComp.get(otherField);
		
		Set<String> results = new HashSet<>();
		for (Resource<?> res : values) {
			//TODO: Check res kind against aKind
			results.add(res.id().label().toString());
		}
		
		return results;
		
	}

}
