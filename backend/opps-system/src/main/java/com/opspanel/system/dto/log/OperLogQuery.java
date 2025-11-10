package com.opspanel.system.dto.log;

import lombok.Data;

/** Query parameters for operation logs. */
@Data
public class OperLogQuery {
    private String operator;
    private Integer status; // optional filter
}
