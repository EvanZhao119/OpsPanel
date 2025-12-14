package com.opspanel.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility methods for working with Collections, Lists, Sets, and Maps.
 *
 * <p>Provides null-safe operations, filtering, merging, and transformations.</p>
 *
 * <p>Compatible with Java 21 and Spring Boot 3.</p>
 *
 * @author OpsPanel
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new AssertionError("No com.opspanel.common.util.CollectionUtils instances for you!");
    }

    /** Check if a collection is null or empty. */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /** Check if a collection is not empty. */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /** Check if a map is null or empty. */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /** Check if a map is not empty. */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /** Returns a new list filtered by the given predicate (null-safe). */
    public static <T> List<T> filter(Collection<T> source, Predicate<? super T> predicate) {
        if (isEmpty(source)) return List.of();
        return source.stream().filter(predicate).collect(Collectors.toList());
    }

    /** Returns a new list mapping each element using the given function (null-safe). */
    public static <T, R> List<R> map(Collection<T> source, Function<? super T, ? extends R> mapper) {
        if (isEmpty(source)) return List.of();
        return source.stream().map(mapper).collect(Collectors.toList());
    }

    /** Merge two collections into a new list (deduplicated). */
    public static <T> List<T> merge(Collection<T> first, Collection<T> second) {
        return List.copyOf(
                        Optional.ofNullable(first).orElse(List.of())
                                .stream()
                                .collect(Collectors.toSet())
                ).stream()
                .collect(Collectors.toList());
    }

    /** Convert a collection to a set (null-safe). */
    public static <T> Set<T> toSet(Collection<T> source) {
        return isEmpty(source) ? Set.of() : new HashSet<>(source);
    }

    /** Convert a collection to a list (null-safe). */
    public static <T> List<T> toList(Collection<T> source) {
        return isEmpty(source) ? List.of() : new ArrayList<>(source);
    }

    /** Joins a collection of strings with the given delimiter. */
    public static String join(Collection<?> collection, String delimiter) {
        if (isEmpty(collection)) return "";
        return collection.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

    /** Return the first element or null if empty. */
    public static <T> T first(Collection<T> collection) {
        return isEmpty(collection) ? null : collection.iterator().next();
    }

    /** Return a single element if the collection has exactly one, otherwise null. */
    public static <T> T single(Collection<T> collection) {
        if (isEmpty(collection) || collection.size() != 1) return null;
        return collection.iterator().next();
    }

    /** Partition a list into chunks of a fixed size. */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (isEmpty(list) || size <= 0) return List.of();
        List<List<T>> parts = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            parts.add(list.subList(i, Math.min(list.size(), i + size)));
        }
        return parts;
    }

    /** Remove null elements from a collection. */
    public static <T> List<T> compact(Collection<T> source) {
        if (isEmpty(source)) return List.of();
        return source.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
