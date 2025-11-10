package com.opspanel.framework.aspect.annotation;

import java.lang.annotation.*;

/**
 * Custom annotation for recording operation logs.
 * Used together with OperLogAspect.
 *
 * Example:
 * @LogOperation(title = "Create User", operator = "admin")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {

    /** Operation title or module name. */
    String title() default "";

    /** Operator name or role. (Optional, default auto-detect) */
    String operator() default "";
}
