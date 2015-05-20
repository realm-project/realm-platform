package net.objectof.model.query.fallback.evaluators;


import java.util.function.BiPredicate;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.Evaluator;
import net.objectof.model.query.fallback.Fallback;


public class GTE implements Evaluator {

    @Override
    public BiPredicate<Object, Object> forStereotype(final Stereotype stereotype) {
        BiPredicate<Object, Object> gt, eq;
        gt = Fallback.GT.forStereotype(stereotype);
        eq = Fallback.EQUAL.forStereotype(stereotype);
        return gt.or(eq);
    }
}
