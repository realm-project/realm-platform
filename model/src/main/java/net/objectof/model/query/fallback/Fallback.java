package net.objectof.model.query.fallback;

import net.objectof.model.query.Relation;
import net.objectof.model.query.fallback.evaluators.Contains;
import net.objectof.model.query.fallback.evaluators.Equal;
import net.objectof.model.query.fallback.evaluators.GT;
import net.objectof.model.query.fallback.evaluators.GTE;
import net.objectof.model.query.fallback.evaluators.LT;
import net.objectof.model.query.fallback.evaluators.LTE;
import net.objectof.model.query.fallback.evaluators.Unequal;

public class Fallback {

	public static final Evaluator EQUAL = new Equal();
	public static final Evaluator UNEQUAL = new Unequal();
	public static final Evaluator CONTAINS = new Contains();
	public static final Evaluator GT = new GT();
	public static final Evaluator GTE = new GTE();
	public static final Evaluator LT = new LT();
	public static final Evaluator LTE = new LTE();
		
	public static Evaluator forRelation(Relation relation) {
		switch (relation) {
			case CONTAINS: return CONTAINS;
			case EQUAL: return EQUAL;
			case GT: return GT;
			case GTE: return GTE;
			case LT: return LT;
			case LTE: return LTE;
			case UNEQUAL: return UNEQUAL;
			default:
				throw new UnsupportedOperationException();
			
		}
	}
	
}
