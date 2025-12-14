package com.opspanel.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Modern String utility class for OpsPanel.
 *
 * <p>Provides null-safe operations, masking, substring helpers,
 * camel/snake conversion, HTTP prefix checks, and Ant-style path matching.</p>
 *
 * <p>Compatible with Java 21 and Spring Boot 3.</p>
 *
 * @author OpsPanel
 */
public final class StringUtilsExt extends StringUtils {

    /** Empty string constant. */
    private static final String EMPTY = "";

    /** Underscore separator. */
    private static final char SEPARATOR = '_';

    /** Asterisk used for masking. */
    private static final char ASTERISK = '*';

    /** Ant-style matcher for URL patterns. */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // ------------------------------------------------------------------------
    // Basic null/blank helpers
    // ------------------------------------------------------------------------

    private StringUtilsExt() {}

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isBlankOrNull(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isBlankOrNull(CharSequence cs) {
        return cs == null || cs.toString().isBlank();
    }

    public static String nullToEmpty(String s) {
        return s == null ? EMPTY : s;
    }

    // ------------------------------------------------------------------------
    // Collection / array checks
    // ------------------------------------------------------------------------

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    // ------------------------------------------------------------------------
    // String operations
    // ------------------------------------------------------------------------

    /** Trim null-safe. */
    public static String trim(String str) {
        return str == null ? EMPTY : str.trim();
    }

    /** Left pad number with zeros. */
    public static String padLeft(Number num, int size) {
        return padLeft(num.toString(), size, '0');
    }

    /** Left pad string with given char. */
    public static String padLeft(String s, int size, char c) {
        if (s == null) s = EMPTY;
        return String.format("%" + c + size + "s", s).replace(' ', c);
    }

    /** Hide a portion of a string with asterisks. */
    public static String mask(String str, int startInclude, int endExclude) {
        if (isBlankOrNull(str)) return EMPTY;
        int length = str.length();
        startInclude = Math.max(0, startInclude);
        endExclude = Math.min(length, endExclude);
        if (startInclude >= endExclude) return str;

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((i >= startInclude && i < endExclude) ? ASTERISK : str.charAt(i));
        }
        return sb.toString();
    }

    /** Substring from given start index (safe for negatives). */
    public static String substring(String str, int start) {
        if (str == null) return EMPTY;
        int len = str.length();
        if (start < 0) start = Math.max(0, len + start);
        return start > len ? EMPTY : str.substring(start);
    }

    /** Substring between two indices (safe for negatives). */
    public static String substring(String str, int start, int end) {
        if (str == null) return EMPTY;
        int len = str.length();
        if (start < 0) start += len;
        if (end < 0) end += len;
        start = Math.max(0, start);
        end = Math.min(len, end);
        return start >= end ? EMPTY : str.substring(start, end);
    }

    /** Convert under_score_case → camelCase. */
    public static String toCamelCase(String s) {
        if (s == null) return null;
        if (s.indexOf(SEPARATOR) == -1) return s;
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (char c : s.toCharArray()) {
            if (c == SEPARATOR) {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Convert CamelCase → under_score_case. */
    public static String toUnderScoreCase(String s) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c) && i > 0) sb.append(SEPARATOR);
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /** Convert list of values to joined string ignoring blanks. */
    public static String joinNonBlank(String delimiter, String... values) {
        return Arrays.stream(values)
                .filter(v -> v != null && !v.isBlank())
                .collect(Collectors.joining(delimiter));
    }

    // ------------------------------------------------------------------------
    // Pattern and match utilities
    // ------------------------------------------------------------------------

    /** Check if link starts with http:// or https:// */
    public static boolean isHttp(String link) {
        return StringUtils.startsWithAny(link, "http://", "https://");
    }

    /** Ant-style URL match (supports *, **, ?). */
    public static boolean isMatch(String pattern, String url) {
        return PATH_MATCHER.match(pattern, url);
    }

    /** Check if any of the array values are contained in a collection. */
    public static boolean containsAny(Collection<String> collection, String... array) {
        if (isEmpty(collection) || isEmpty(array)) return false;
        for (String s : array) {
            if (collection.contains(s)) return true;
        }
        return false;
    }

    /** Case-insensitive containment across multiple targets. */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        if (isBlankOrNull(cs) || searchCharSequences == null || searchCharSequences.length == 0)
            return false;
        for (CharSequence candidate : searchCharSequences) {
            if (StringUtils.containsIgnoreCase(cs, candidate)) return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------
    // Converters & formatters
    // ------------------------------------------------------------------------

    /** Format string using standard Java format placeholders. */
    public static String format(String template, Object... args) {
        if (isBlankOrNull(template) || args == null || args.length == 0)
            return template;
        return String.format(template.replace("{}", "%s"), args);
    }

    /** Safe cast utility. */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
