package net.objectof.model.query.fallback.evaluators;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.BiPredicate;
import net.objectof.model.query.fallback.Evaluator;

public class Equal implements Evaluator {

	@Override
	public BiPredicate<Object, Object> forStereotype(Stereotype stereotype) {
		return new BiPredicate<Object, Object>() {

			@Override
			public boolean test(Object t, Object u) {
				if (t == null && u == null) { return true; }
				if (t == null) { return false; }
				return t.equals(u);
			}
		};
	}

}
