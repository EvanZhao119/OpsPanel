package com.opspanel.system.dto.role;

import lombok.Data;

/** Query parameters for role search. */
@Data
public class RoleQuery {
    private String roleName;
    private Integer status;
}
