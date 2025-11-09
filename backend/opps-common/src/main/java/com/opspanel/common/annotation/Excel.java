package com.opspanel.common.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.*;
import java.math.BigDecimal;

/**
 * Excel column mapping annotation
 * Defines how a field is mapped to an Excel column
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /** Excel column name */
    String name() default "";

    /** Date format, e.g., yyyy-MM-dd */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /** Column width in characters */
    double width() default 20;

    /** Sorting order, smaller appears first */
    int sort() default Integer.MAX_VALUE;

    /** Text alignment */
    HorizontalAlignment align() default HorizontalAlignment.LEFT;

    /** Header text color */
    IndexedColors headerColor() default IndexedColors.BLACK;

    /** Background color */
    IndexedColors backgroundColor() default IndexedColors.WHITE;

    /** Whether this field is exported */
    boolean isExport() default true;

    /** Merge same-value cells */
    boolean needMerge() default false;

    /** Data type */
    ColumnType cellType() default ColumnType.STRING;

    /** Operation type */
    Type type() default Type.ALL;

    /** Conversion expression, e.g., 0=Male,1=Female */
    String readConverterExp() default "";

    /** Dictionary type name for lookup */
    String dictType() default "";

    /** Suffix appended to displayed value */
    String suffix() default "";

    /** Default value if null */
    String defaultValue() default "";

    /** Nested property name */
    String targetAttr() default "";

    /** Wrap text */
    boolean wrapText() default false;

    /** Include in totals */
    boolean isStatistics() default false;

    /** Decimal precision */
    int scale() default -1;

    /** Rounding mode */
    int roundingMode() default BigDecimal.ROUND_HALF_UP;

    /** Custom formatter */
    Class<?> handler() default ExcelHandlerAdapter.class;

    /** Arguments for formatter */
    String[] args() default {};

    enum ColumnType { NUMERIC, STRING, IMAGE, TEXT }

    enum Type { ALL, EXPORT, IMPORT }
}
