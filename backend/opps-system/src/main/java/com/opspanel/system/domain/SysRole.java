package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long roleId;
    private String roleName;
    private String roleKey;     // e.g. ADMIN
    private Integer sort;
    private Integer status;     // 1=enabled
    @TableLogic
    private Integer deleted;
}
