package net.objectof.model.query.fallback.evaluators;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.BiPredicate;
import net.objectof.model.query.fallback.Evaluator;
import net.objectof.model.query.fallback.Fallback;

public class Unequal implements Evaluator {

	@Override
	public BiPredicate<Object, Object> method(Stereotype stereotype) {
		return Fallback.EQUAL.method(stereotype).negate();
	}

}
