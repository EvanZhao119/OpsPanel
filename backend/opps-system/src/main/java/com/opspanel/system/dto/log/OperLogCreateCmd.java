package com.opspanel.system.dto.log;

import lombok.Data;
import java.time.LocalDateTime;

/** Command object for creating operation logs manually or via AOP. */
@Data
public class OperLogCreateCmd {
    private String title;
    private String method;
    private String requestUri;
    private String requestMethod;
    private String operator;
    private LocalDateTime operTime = LocalDateTime.now();
    private Integer status; // 1=success, 0=failure
    private String errorMsg;
}
