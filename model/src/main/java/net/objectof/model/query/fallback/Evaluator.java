package net.objectof.model.query.fallback;


import java.util.function.BiPredicate;

import net.objectof.model.Stereotype;


public interface Evaluator {

    BiPredicate<Object, Object> forStereotype(Stereotype stereotype);
}
