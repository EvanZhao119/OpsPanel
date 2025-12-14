package com.opspanel.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/**
 * Date and time utility class for OpsPanel.
 *
 * <p>Built with Java 8+ time API for thread safety and international compatibility.</p>
 *
 * <p>Provides common operations for conversion, formatting, and date difference calculation.</p>
 *
 * @author OpsPanel
 */
public final class DateUtils {

    private DateUtils() {
        // Prevent instantiation
    }

    /* -------------------- Standard Date Formats -------------------- */

    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";


    /* -------------------- Formatters -------------------- */

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern(YYYY_MM_DD);
    public static final DateTimeFormatter FORMAT_DATETIME = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    public static final DateTimeFormatter FORMAT_COMPACT = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);


    /* -------------------- Current Time -------------------- */

    /** Get current LocalDateTime */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /** Get current LocalDate */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /** Get current system time as formatted string */
    public static String nowString(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /** Get current time with default format yyyy-MM-dd HH:mm:ss */
    public static String nowString() {
        return now().format(FORMAT_DATETIME);
    }


    /* -------------------- Conversion -------------------- */

    /** Convert java.util.Date → LocalDateTime */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (Objects.isNull(date)) return null;
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /** Convert LocalDateTime → java.util.Date */
    public static Date toDate(LocalDateTime ldt) {
        if (Objects.isNull(ldt)) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /** Convert LocalDate → java.util.Date (00:00:00 local time) */
    public static Date toDate(LocalDate ld) {
        if (Objects.isNull(ld)) return null;
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /** Parse string to LocalDateTime */
    public static LocalDateTime parse(String text, String pattern) {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    /** Format LocalDateTime to string */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    /* -------------------- Difference Calculation -------------------- */

    /** Calculate difference in days between two dates */
    public static long diffDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /** Calculate difference in minutes between two times */
    public static long diffMinutes(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

    /** Calculate difference in human-readable string (e.g. "2 days 3 hours 15 mins") */
    public static String diffReadable(LocalDateTime start, LocalDateTime end) {
        long totalMinutes = ChronoUnit.MINUTES.between(start, end);
        long days = totalMinutes / (60 * 24);
        long hours = (totalMinutes % (60 * 24)) / 60;
        long minutes = totalMinutes % 60;
        return String.format("%d days %d hours %d mins", days, hours, minutes);
    }


    /* -------------------- Miscellaneous -------------------- */

    /** Get the server startup time (JVM start time). */
    public static LocalDateTime getServerStartTime() {
        long startMillis = java.lang.management.ManagementFactory.getRuntimeMXBean().getStartTime();
        return Instant.ofEpochMilli(startMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
