package net.objectof.model.query.fallback.evaluators;

import java.util.Date;

import net.objectof.aggr.Aggregate;
import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.BiPredicate;
import net.objectof.model.query.fallback.Evaluator;

public class LT implements Evaluator {

	@Override
	public BiPredicate<Object, Object> method(final Stereotype stereotype) {
		return new BiPredicate<Object, Object>() {

			@Override
			public boolean test(Object t, Object u) {
				
				if (t == null && u == null) { return false; }
				if (t == null) { return true; }
				
				switch (stereotype) {
					case INDEXED:
					case MAPPED:
					case SET:
						return ((Aggregate<?, ?>)t).size() < ((Aggregate<?, ?>)u).size();
					case TEXT:
						return ((String)t).compareTo((String)u) < 0;
					case INT:
						return (Long)t < (Long)u;
					case NUM:
						return (Double)t < (Double)u;
					case MOMENT:
						return ((Date)t).before((Date)u);
						
					default:
						throw new UnsupportedOperationException();
					
				}
			}};
	}

}
