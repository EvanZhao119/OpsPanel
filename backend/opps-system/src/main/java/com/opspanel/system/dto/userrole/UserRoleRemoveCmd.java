package com.opspanel.system.dto.userrole;

import lombok.Data;

/** Command for removing all role relations for a user. */
@Data
public class UserRoleRemoveCmd {
    private Long userId;
}
