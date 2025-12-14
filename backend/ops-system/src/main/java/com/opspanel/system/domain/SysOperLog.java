package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** Entity representing a system operation log. */
@Data
@TableName("sys_oper_log")
public class SysOperLog {
    @TableId(type = IdType.AUTO)
    private Long operId;

    private String title;
    private String method;
    private String requestUri;
    private String requestMethod;
    private String operator;
    private LocalDateTime operTime;
    private Integer status; // 1 = success, 0 = fail
    private String errorMsg;
}
