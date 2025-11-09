package com.opspanel.framework.annotation;

import java.lang.annotation.*;

/**
 * Annotation for logging method execution.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}
