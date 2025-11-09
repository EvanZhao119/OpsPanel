package com.opspanel.common.exception;

/**
 * Centralized registry for standard application error codes and default messages.
 *
 * <p>Defines both HTTP-aligned and business-specific codes.
 * Helps unify error handling and API response payloads.</p>
 *
 * <p>Compatible with Java 21 and Spring Boot 3.</p>
 *
 * @author OpsPanel
 */
public enum GlobalErrorCode {

    // --- General success / failure ---
    SUCCESS(200, "Operation successful"),
    FAILED(500, "Internal server error"),

    // --- Validation & input errors ---
    BAD_REQUEST(400, "Invalid request parameters"),
    UNAUTHORIZED(401, "Authentication required or failed"),
    FORBIDDEN(403, "Access denied"),
    NOT_FOUND(404, "Resource not found"),
    CONFLICT(409, "Resource conflict"),
    VALIDATION_ERROR(422, "Request validation failed"),

    // --- Business domain errors ---
    USER_NOT_FOUND(1001, "User not found"),
    DUPLICATE_ENTRY(1002, "Duplicate data entry"),
    INSUFFICIENT_PERMISSIONS(1003, "Insufficient permissions"),
    OPERATION_NOT_ALLOWED(1004, "Operation not allowed"),

    // --- System / integration errors ---
    SERVICE_UNAVAILABLE(2001, "External service unavailable"),
    DATABASE_ERROR(2002, "Database access error"),
    TIMEOUT(2003, "Operation timed out"),

    // --- Excel-related errors ---
    EXCEL_EXPORT_ERROR(3001, "Excel export failed"),
    EXCEL_IMPORT_ERROR(3002, "Excel import failed"),

    // --- Default catch-all ---
    UNKNOWN_ERROR(9999, "Unknown system error");

    private final int code;
    private final String message;

    GlobalErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    /** Resolve an enum by code, or return UNKNOWN_ERROR if not found. */
    public static GlobalErrorCode fromCode(int code) {
        for (GlobalErrorCode value : values()) {
            if (value.code == code) return value;
        }
        return UNKNOWN_ERROR;
    }
}
