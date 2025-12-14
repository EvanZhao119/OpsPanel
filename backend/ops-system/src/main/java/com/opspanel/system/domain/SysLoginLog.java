package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** Entity representing a system login log. */
@Data
@TableName("sys_login_log")
public class SysLoginLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String ipaddr;
    private LocalDateTime loginTime;
    private Integer status; // 1 = success, 0 = fail
    private String msg;
}
