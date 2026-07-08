package com.sk89q.worldedit.util.command.parametric;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindingMatch {
   Class<? extends Annotation> classifier() default Annotation.class;

   Class<?>[] type() default {Class.class};

   BindingBehavior behavior();

   int consumedCount() default -1;

   boolean provideModifiers() default false;
}
