package net.objectof.model.query.fallback;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;

import net.objectof.model.Kind;
import net.objectof.model.Package;
import net.objectof.model.Transaction;
import net.objectof.model.impl.aggr.IComposite;
import net.objectof.model.query.QueryResolver;
import net.objectof.model.query.Relation;

public class FallbackResolver implements QueryResolver {
	
	private Package repo;
	private Kind<?> theKind;
	
	public FallbackResolver(Package repo, String aKind) {
		this.repo = repo;
		
		for (Kind<?> kind : repo.getParts()) {
			if (kind.getPartOf() != null) { continue; }
			if (kind.getComponentName().equals(aKind)) {
				theKind = kind;
			}
		}
		
		if (theKind == null) {
			throw new IllegalArgumentException();
		}
		
	}
	
	@Override
	public Set<String> resolve(int aLimit, String key, Relation relation, Object value) {
		
		Transaction tx = repo.connect("NativeQueryResolver");
		Iterable<IComposite> iter = tx.enumerate(theKind.getComponentName());
		Set<String> labels = new HashSet<>();
		
		
		//determine the kind and stereotype of the key we're looking at
		Kind<?> keyKind = null;
		
		for (Kind<?> kind : theKind.getParts()) {
			String field = kind.getComponentName().split("\\.", 2)[1];
			if (field.equals(key)) {
				keyKind = kind;
			}
		}
		if (keyKind == null) {
			throw new IllegalArgumentException();
		}
		
		//get the native/fallback relation test for this stereotype
		BiPredicate<Object, Object> proc = Fallback.forRelation(relation).forStereotype(keyKind.getStereotype());
		
		//go over every item in the enumeration and apply the test
		int count = 0;
		for (IComposite comp : iter) {
			Object o = comp.get(key);
			boolean result = proc.test(o, value);
			if (result) {
				labels.add(comp.id().label().toString());
				count++;
			}
			if (count >= aLimit) { break; }
		}
		
		return labels;
		
	}

}
