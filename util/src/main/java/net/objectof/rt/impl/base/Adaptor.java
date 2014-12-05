package net.objectof.rt.impl.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Adaptor
{
  Class<?> value();
}
