package com.opspanel.system.dto.role;

import lombok.Data;

/** Command for updating a role. */
@Data
public class RoleUpdateCmd {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private Integer status;
}
