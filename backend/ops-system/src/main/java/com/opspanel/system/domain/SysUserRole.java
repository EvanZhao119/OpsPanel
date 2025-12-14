package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** Mapping between User and Role. */
@Data
@TableName("sys_user_role")
public class SysUserRole {
    private Long userId;
    private Long roleId;
}
