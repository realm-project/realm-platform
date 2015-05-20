package net.objectof.model.query.fallback.evaluators;


import java.util.function.BiPredicate;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.Evaluator;


public class Equal implements Evaluator {

    @Override
    public BiPredicate<Object, Object> forStereotype(Stereotype stereotype) {
        return (t, u) -> {
            if (t == null && u == null) { return true; }
            if (t == null) { return false; }
            return t.equals(u);
        };
    }
}
