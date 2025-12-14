package com.opspanel.common.util;

import java.util.Objects;
import java.util.Optional;

/**
 * Utility methods for common Object operations.
 *
 * <p>Null-safe equality, emptiness checks, defaulting, and deep comparison.</p>
 *
 * <p>Compatible with Java 21 and Spring Boot 3.</p>
 *
 * @author OpsPanel
 */
public final class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError("No com.opspanel.common.util.ObjectUtils instances for you!");
    }

    /** Check if an object is null. */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /** Check if an object is not null. */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * Return {@code defaultValue} if {@code obj} is null.
     */
    public static <T> T defaultIfNull(T obj, T defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * Returns the first non-null value among the given arguments.
     */
    @SafeVarargs
    public static <T> T firstNonNull(T... values) {
        if (values == null) return null;
        for (T value : values) {
            if (value != null) return value;
        }
        return null;
    }

    /**
     * Deep equality check using {@link java.util.Objects#deepEquals(Object, Object)}.
     */
    public static boolean deepEquals(Object a, Object b) {
        return Objects.deepEquals(a, b);
    }

    /**
     * Returns true if the object is considered empty.
     * Supports CharSequence, arrays, Collections, and Maps.
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof CharSequence seq) return seq.isEmpty();
        if (obj instanceof java.util.Collection<?> coll) return coll.isEmpty();
        if (obj instanceof java.util.Map<?, ?> map) return map.isEmpty();
        if (obj.getClass().isArray()) return java.lang.reflect.Array.getLength(obj) == 0;
        return false;
    }

    /** Negation of {@link #isEmpty(Object)}. */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /** Safe string conversion, returns empty string if null. */
    public static String toString(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse("");
    }

    /** Safe string conversion with default. */
    public static String toString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : obj.toString();
    }

    /** Compare two objects for equality. */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /** Hash code helper. */
    public static int hash(Object... values) {
        return Objects.hash(values);
    }
}
