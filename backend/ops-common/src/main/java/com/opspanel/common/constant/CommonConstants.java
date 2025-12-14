package com.opspanel.common.constant;

/**
 * Global constant definitions used across OpsPanel modules.
 *
 * <p>This class centralizes common constant values
 * that are shared by multiple layers and modules.</p>
 *
 * @author OpsPanel
 */
public final class CommonConstants {

    private CommonConstants() {
        // Prevent instantiation
    }

    /* -------------------- Encoding & Format -------------------- */

    /** UTF-8 character encoding (default for all APIs). */
    public static final String UTF8 = "UTF-8";

    /** Application JSON content type. */
    public static final String APPLICATION_JSON = "application/json";

    /** Generic success string constant. */
    public static final String SUCCESS = "SUCCESS";

    /** Generic failure string constant. */
    public static final String FAIL = "FAIL";

    /** Default time zone for OpsPanel services. */
    public static final String DEFAULT_TIMEZONE = "UTC";


    /* -------------------- Boolean Values -------------------- */

    /** Boolean flag for "Yes". */
    public static final String YES = "Y";

    /** Boolean flag for "No". */
    public static final String NO = "N";


    /* -------------------- Symbol Constants -------------------- */

    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String COLON = ":";
    public static final String UNDERLINE = "_";
    public static final String DASH = "-";


    /* -------------------- Default Values -------------------- */

    /** Default page number for paginated queries. */
    public static final int DEFAULT_PAGE_NUM = 1;

    /** Default page size for paginated queries. */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** Maximum allowed page size to prevent over-fetching. */
    public static final int MAX_PAGE_SIZE = 1000;


    /* -------------------- Misc -------------------- */

    /** Header key for authorization tokens. */
    public static final String AUTH_HEADER = "Authorization";

    /** Prefix for JWT tokens in HTTP headers. */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** Default date format used in API responses. */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
