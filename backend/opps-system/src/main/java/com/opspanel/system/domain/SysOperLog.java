package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_oper_log")
public class SysOperLog {
    @TableId(type = IdType.AUTO)
    private Long operId;
    private String title;
    private String method;
    private String requestMethod;
    private String operName;
    private String operUrl;
    private String operIp;
    private Integer status; // 0=success,1=fail
    private String errorMsg;
    private LocalDateTime operTime;
}
