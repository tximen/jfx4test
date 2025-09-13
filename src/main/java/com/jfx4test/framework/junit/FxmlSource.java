package com.jfx4test.framework.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface FxmlSource {

    String value() default "";
    double width()  default -1d;
    double height() default -1d;
    long   delayInSeconds() default -1L;
}
