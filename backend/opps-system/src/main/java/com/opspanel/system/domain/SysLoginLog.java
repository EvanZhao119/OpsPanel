package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_login_log")
public class SysLoginLog {
    @TableId(type = IdType.AUTO)
    private Long loginId;
    private String username;
    private String ipaddr;
    private Integer status; // 0=success,1=fail
    private String msg;
    private LocalDateTime loginTime;
}
