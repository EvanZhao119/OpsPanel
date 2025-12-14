package com.opspanel.system.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.dto.rolemenu.RoleMenuAssignCmd;
import com.opspanel.system.dto.rolemenu.RoleMenuQuery;
import com.opspanel.system.service.SysRoleMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** REST controller for managing role-menu relationships. */
@RestController
@RequestMapping("/api/system/role-menu")
public class SysRoleMenuController {

    private final SysRoleMenuService roleMenuService;

    public SysRoleMenuController(SysRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    /** Assign multiple menus to a role. */
    @PostMapping("/assign")
    public ApiResponse<Boolean> assign(@RequestBody RoleMenuAssignCmd cmd) {
        roleMenuService.assignMenus(cmd.getRoleId(), cmd.getMenuIds());
        return ApiResponse.ok(true);
    }

    /** Remove all menus of a role. */
    @DeleteMapping("/{roleId}")
    public ApiResponse<Boolean> remove(@PathVariable Long roleId) {
        roleMenuService.removeMenus(roleId);
        return ApiResponse.ok(true);
    }

    /** Retrieve menu IDs assigned to a role. */
    @GetMapping("/list")
    public ApiResponse<List<Long>> list(RoleMenuQuery query) {
        return ApiResponse.ok(roleMenuService.getMenuIdsByRole(query.getRoleId()));
    }
}
