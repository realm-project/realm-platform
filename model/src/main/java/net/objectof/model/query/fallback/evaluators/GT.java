package net.objectof.model.query.fallback.evaluators;


import java.util.Date;
import java.util.function.BiPredicate;

import net.objectof.aggr.Aggregate;
import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.Evaluator;


public class GT implements Evaluator {

    @Override
    public BiPredicate<Object, Object> forStereotype(final Stereotype stereotype) {
        return (t, u) -> {
            if (t == null && u == null) { return false; }
            if (t == null) { return false; }
            switch (stereotype) {
                case INDEXED:
                case MAPPED:
                case SET:
                    return ((Aggregate<?, ?>) t).size() > ((Aggregate<?, ?>) u).size();
                case TEXT:
                    return ((String) t).compareTo((String) u) > 0;
                case INT:
                    return (Long) t > (Long) u;
                case NUM:
                    return (Double) t > (Double) u;
                case MOMENT:
                    return ((Date) t).after((Date) u);
                default:
                    throw new UnsupportedOperationException();
            }
        };
    }
}
