package com.opspanel.system.dto.role;

import lombok.Data;

/** Create command for role. */
@Data
public class RoleCreateCmd {
    public String roleName;
    public String roleCode;
    public Integer status;
    public Integer sort;
}

