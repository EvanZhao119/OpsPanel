package com.opspanel.system.dto.rolemenu;

import lombok.Data;

/** Query parameters for retrieving menus bound to a role. */
@Data
public class RoleMenuQuery {
    private Long roleId;
}
