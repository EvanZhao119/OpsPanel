package com.opspanel.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Framework event representing an operation log to be recorded.
 */
@Data
@AllArgsConstructor
public class OperLogEvent {
    private String title;
    private String method;
    private String requestUri;
    private String requestMethod;
    private String operator;
    private LocalDateTime operTime;
    private Integer status;
    private String errorMsg;
}
