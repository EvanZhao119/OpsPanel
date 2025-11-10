package com.opspanel.framework.web.advice;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.common.exception.BusinessException;
import com.opspanel.common.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for unified error responses.
 * <p>
 * Converts various exceptions into structured {@link ApiResponse} objects.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom business exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        log.warn("BusinessException: {}", ex.getMessage());
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * Handles unexpected system-level exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ApiResponse.error(GlobalErrorCode.UNKNOWN_ERROR.code(),
                GlobalErrorCode.UNKNOWN_ERROR.message());
    }
}
