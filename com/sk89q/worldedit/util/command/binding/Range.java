package com.sk89q.worldedit.util.command.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Range {
   double min() default Double.MIN_VALUE;

   double max() default Double.MAX_VALUE;
}
