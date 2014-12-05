package net.objectof;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * A Selector marks a Java element for inclusion in the objectof framework. The
 * default value acts to mark the element for reflection with a default
 * selector, based on the code processing the annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Selector {

    public String value() default "";
}
