package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimpleFunctionAnnotation {
    String name() default "";
    int priority() default 0;
}