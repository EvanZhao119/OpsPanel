package com.opspanel.common.annotation;

import java.lang.annotation.*;

/**
 * Container annotation for multiple @Excel annotations
 * Useful for nested objects or multiple column mappings.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excels {
    Excel[] value();
}
