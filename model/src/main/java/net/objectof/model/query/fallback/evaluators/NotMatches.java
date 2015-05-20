package net.objectof.model.query.fallback.evaluators;


import java.util.function.BiPredicate;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.Evaluator;
import net.objectof.model.query.fallback.Fallback;


public class NotMatches implements Evaluator {

    @Override
    public BiPredicate<Object, Object> forStereotype(Stereotype stereotype) {
        return Fallback.MATCHES.forStereotype(stereotype).negate();
    }
}
