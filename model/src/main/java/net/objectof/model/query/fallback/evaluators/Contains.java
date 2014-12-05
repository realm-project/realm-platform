package net.objectof.model.query.fallback.evaluators;

import net.objectof.model.Stereotype;
import net.objectof.model.impl.aggr.IIndexed;
import net.objectof.model.impl.aggr.IMapped;
import net.objectof.model.query.fallback.BiPredicate;
import net.objectof.model.query.fallback.Evaluator;

public class Contains implements Evaluator {

	@Override
	public BiPredicate<Object, Object> method(final Stereotype stereotype) {
		return new BiPredicate<Object, Object>() {

			@Override
			public boolean test(Object t, Object u) {
				if (t == null) { return false; }
				
				switch (stereotype) {

					case INDEXED:
						return ((IIndexed<?>) t).contains(u);
					case MAPPED:
						return ((IMapped<?>) t).containsValue(u);
					case TEXT:
						return ((String)t).contains((String)u);
					default:
						throw new UnsupportedOperationException();
				}
			}
		};
	}


}
