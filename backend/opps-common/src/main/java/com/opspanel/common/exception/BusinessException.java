package com.opspanel.common.exception;

/**
 * Custom runtime exception for business logic and domain validation errors.
 *
 * <p>Wraps a {@link GlobalErrorCode} with an optional detail message.
 * Used to indicate recoverable or expected errors at service level.</p>
 *
 * @author OpsPanel
 */
public class BusinessException extends RuntimeException {

    private final int code;
    private final String message;

    /** Construct directly from GlobalErrorCode. */
    public BusinessException(GlobalErrorCode errorCode) {
        super(errorCode.message());
        this.code = errorCode.code();
        this.message = errorCode.message();
    }

    /** Construct from GlobalErrorCode with custom message. */
    public BusinessException(GlobalErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.code();
        this.message = customMessage;
    }

    /** Construct from numeric code and message directly. */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /** Shortcut: throw BusinessException.of(GlobalErrorCode.XYZ) */
    public static BusinessException of(GlobalErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    /** Shortcut with custom message */
    public static BusinessException of(GlobalErrorCode errorCode, String customMessage) {
        return new BusinessException(errorCode, customMessage);
    }
}
