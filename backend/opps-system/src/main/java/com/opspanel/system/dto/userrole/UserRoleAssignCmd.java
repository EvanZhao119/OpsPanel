package com.opspanel.system.dto.userrole;

import lombok.Data;
import java.util.List;

/** Command for assigning roles to a user. */
@Data
public class UserRoleAssignCmd {
    private Long userId;
    private List<Long> roleIds;
}
