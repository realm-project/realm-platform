package net.objectof.model.query.fallback.evaluators;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.BiPredicate;
import net.objectof.model.query.fallback.Evaluator;
import net.objectof.model.query.fallback.Fallback;

public class GTE implements Evaluator {

	@Override
	public BiPredicate<Object, Object> method(final Stereotype stereotype) {
		BiPredicate<Object, Object> gt, eq;
		
		gt = Fallback.GT.method(stereotype);
		eq = Fallback.EQUAL.method(stereotype);
		
		return gt.or(eq);
	}

}
