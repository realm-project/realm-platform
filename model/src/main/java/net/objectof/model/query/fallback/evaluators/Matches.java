package net.objectof.model.query.fallback.evaluators;


import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.objectof.model.Stereotype;
import net.objectof.model.query.fallback.Evaluator;


public class Matches implements Evaluator {

    @Override
    public BiPredicate<Object, Object> forStereotype(Stereotype stereotype) {
        return (testString, patternString) -> {
            if (testString == null && patternString == null) { return true; }
            if (testString == null || patternString == null) { return false; }
            Pattern p = Pattern.compile(patternString.toString());
            Matcher m = p.matcher(testString.toString());
            return m.matches();
        };
    }
}
