package com.opspanel.system.dto.role;

import lombok.Data;

/** Command for creating a role. */
@Data
public class RoleCreateCmd {
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private Integer status;
}
