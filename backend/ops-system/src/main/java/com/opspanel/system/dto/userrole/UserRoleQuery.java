package com.opspanel.system.dto.userrole;

import lombok.Data;

/** Query parameters for retrieving roles bound to a user. */
@Data
public class UserRoleQuery {
    private Long userId;
}
