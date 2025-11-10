package com.opspanel.system.dto.log;

import lombok.Data;

/** Query parameters for login logs. */
@Data
public class LoginLogQuery {
    private String username;
    private Integer status; // optional filter
}

