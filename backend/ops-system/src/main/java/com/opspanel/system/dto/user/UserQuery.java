package com.opspanel.system.dto.user;

import lombok.Data;

/** Query parameters for user pagination. */
@Data
public class UserQuery {
    private String username;
    private Integer status;
    private Long deptId;
}
