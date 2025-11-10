package com.opspanel.system.dto.rolemenu;

import lombok.Data;
import java.util.List;

/** Command for assigning menus to a role. */
@Data
public class RoleMenuAssignCmd {
    private Long roleId;
    private List<Long> menuIds;
}
