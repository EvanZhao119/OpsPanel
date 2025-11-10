package com.opspanel.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** Mapping between Role and Menu. */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu {
    private Long roleId;
    private Long menuId;
}
