package com.opspanel.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for precise time and duration operations.
 *
 * <p>Focuses on high-resolution system time, durations, and UTC conversion.</p>
 */
public final class TimeUtils {

    private TimeUtils() {}

    /** Returns current epoch milliseconds (UTC). */
    public static long nowMillis() {
        return Instant.now().toEpochMilli();
    }

    /** Returns current epoch seconds (UTC). */
    public static long nowSeconds() {
        return Instant.now().getEpochSecond();
    }

    /** Returns system nano time (useful for measuring elapsed time). */
    public static long nowNano() {
        return System.nanoTime();
    }

    /** Measures elapsed time in milliseconds between two nanos. */
    public static long elapsedMillis(long startNano) {
        return (System.nanoTime() - startNano) / 1_000_000L;
    }

    /** Formats duration into HH:mm:ss */
    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /** Converts LocalDateTime to UTC ISO string. */
    public static String toUTCString(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT);
    }
}
