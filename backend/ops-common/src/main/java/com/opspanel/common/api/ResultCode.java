package com.opspanel.common.api;

/**
 * Standardized result and HTTP-like status codes for OpsPanel responses.
 *
 * <p>This class aligns with common HTTP semantics while reserving
 * custom codes for internal application logic (e.g., WARN = 601).</p>
 *
 * @author OpsPanel
 */
public final class ResultCode {

    private ResultCode() {
        // Prevent instantiation
    }

    /* -------------------- Success -------------------- */

    /** Operation succeeded (OK). */
    public static final int SUCCESS = 200;

    /** Resource successfully created. */
    public static final int CREATED = 201;

    /** Request accepted for asynchronous processing. */
    public static final int ACCEPTED = 202;

    /** Operation succeeded but no content returned. */
    public static final int NO_CONTENT = 204;


    /* -------------------- Redirection -------------------- */

    /** Permanent redirect. */
    public static final int MOVED_PERMANENTLY = 301;

    /** Temporary redirect (see other). */
    public static final int SEE_OTHER = 303;

    /** Resource not modified since last request. */
    public static final int NOT_MODIFIED = 304;


    /* -------------------- Client Errors -------------------- */

    /** Bad request – missing or invalid parameters. */
    public static final int BAD_REQUEST = 400;

    /** Unauthorized – authentication required. */
    public static final int UNAUTHORIZED = 401;

    /** Forbidden – valid credentials but no permission. */
    public static final int FORBIDDEN = 403;

    /** Resource not found. */
    public static final int NOT_FOUND = 404;

    /** Method not allowed. */
    public static final int METHOD_NOT_ALLOWED = 405;

    /** Conflict – resource already exists or locked. */
    public static final int CONFLICT = 409;

    /** Unsupported media type. */
    public static final int UNSUPPORTED_MEDIA_TYPE = 415;


    /* -------------------- Server Errors -------------------- */

    /** Internal server error. */
    public static final int INTERNAL_ERROR = 500;

    /** Not implemented – interface or feature not supported. */
    public static final int NOT_IMPLEMENTED = 501;


    /* -------------------- Custom Application Codes -------------------- */

    /** Business warning (non-standard). */
    public static final int WARN = 601;
}
