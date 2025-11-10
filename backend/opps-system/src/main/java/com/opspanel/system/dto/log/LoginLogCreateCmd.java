package com.opspanel.system.dto.log;

import lombok.Data;
import java.time.LocalDateTime;

/** Command object for creating login logs manually. */
@Data
public class LoginLogCreateCmd {
    private String username;
    private String ipaddr;
    private LocalDateTime loginTime = LocalDateTime.now();
    private Integer status; // 1=success, 0=failure
    private String msg;
}
