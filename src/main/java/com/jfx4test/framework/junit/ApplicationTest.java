package com.jfx4test.framework.junit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(ApplicationExtension.class)
public @interface ApplicationTest {

    String value() default "";
    double width()  default -1d;
    double height() default -1d;
    long   delayInSeconds() default -1L;
}
