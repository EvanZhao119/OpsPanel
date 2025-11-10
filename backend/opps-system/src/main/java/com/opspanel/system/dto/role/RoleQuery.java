package com.opspanel.system.dto.role;

import lombok.Data;

@Data
/** Query filter for roles. */
public class RoleQuery {
    public String roleName;
    public String roleCode;
    public Integer status;
}
