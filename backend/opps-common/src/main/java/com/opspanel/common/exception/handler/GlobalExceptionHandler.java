package com.opspanel.common.exception.handler;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.common.api.ResultCode;
import com.opspanel.common.exception.BusinessException;
import com.opspanel.common.exception.GlobalErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for unified error responses.
 *
 * <p>Handles both business and system-level exceptions consistently.
 * Ensures API always returns a structured {@link ApiResponse}.</p>
 *
 * @author OpsPanel
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle custom business logic exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        log.warn("Business exception: [{}] {}", ex.getCode(), ex.getMessage());
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * Handle validation errors from @Valid and @Validated annotations.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<Void> handleValidationException(Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException e) {
            message = e.getBindingResult().getFieldError() != null
                    ? e.getBindingResult().getFieldError().getDefaultMessage()
                    : "Validation failed";
        } else if (ex instanceof BindException e) {
            message = e.getBindingResult().getFieldError() != null
                    ? e.getBindingResult().getFieldError().getDefaultMessage()
                    : "Validation failed";
        } else {
            message = "Validation failed";
        }
        log.warn("Validation exception: {}", message);
        return ApiResponse.error(GlobalErrorCode.VALIDATION_ERROR.code(), message);
    }

    /**
     * Handle constraint violations for query parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().iterator().next().getMessage();
        log.warn("Constraint violation: {}", message);
        return ApiResponse.error(GlobalErrorCode.VALIDATION_ERROR.code(), message);
    }

    /**
     * Handle malformed JSON or missing request body.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON or missing request body", ex);
        return ApiResponse.error(GlobalErrorCode.BAD_REQUEST.code(), "Malformed JSON request");
    }

    /**
     * Handle all uncaught exceptions (fallback).
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGenericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ApiResponse.error(
                GlobalErrorCode.UNKNOWN_ERROR.code(),
                GlobalErrorCode.UNKNOWN_ERROR.message()
        );
    }
}
