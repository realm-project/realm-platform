package net.objectof.model.query.fallback;

import net.objectof.model.Stereotype;

public interface Evaluator {

	BiPredicate<Object, Object> method(Stereotype stereotype);
	
}
