package com.opspanel.system.dto.rolemenu;

import lombok.Data;

/** Command for removing all menu relations for a role. */
@Data
public class RoleMenuRemoveCmd {
    private Long roleId;
}
