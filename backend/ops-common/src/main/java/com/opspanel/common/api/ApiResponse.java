package com.opspanel.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Unified API response wrapper.
 * @param <T> type of response data
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Status code */
    private final int code;

    /** Message */
    private final String message;

    /** Data object */
    private final T data;

    /** Optional extra fields for flexibility (chainable put) */
    private final Map<String, Object> extra = new HashMap<>();

    /** Constructors */
    public ApiResponse(int code, String message) {
        this(code, message, null);
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** ====================  Static Factory Methods  ==================== */

    public static <T> ApiResponse<T> ok() {
        return ok("Operation successful");
    }

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(Code.SUCCESS.value, message, null);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(Code.SUCCESS.value, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(Code.SUCCESS.value, "Operation successful", data);
    }

    public static <T> ApiResponse<T> warn(String message) {
        return new ApiResponse<>(Code.WARN.value, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(Code.ERROR.value, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(Code.ERROR.value, message, data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }


    /** ====================  Enum for Response Codes  ==================== */

    public enum Code {
        SUCCESS(200),
        WARN(301),
        ERROR(500);

        public final int value;

        Code(int value) {
            this.value = value;
        }
    }

    /** ====================  Helper Methods  ==================== */

    public boolean isSuccess() {
        return code == Code.SUCCESS.value;
    }

    public boolean isWarn() {
        return code == Code.WARN.value;
    }

    public boolean isError() {
        return code == Code.ERROR.value;
    }

    /** Chainable field for extra metadata (timestamp, traceId, etc.) */
    public ApiResponse<T> put(String key, Object value) {
        extra.put(key, value);
        return this;
    }

    public Object get(String key) {
        return extra.get(key);
    }

    /** ====================  Getters  ==================== */

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", extra=" + extra +
                '}';
    }
}
