package com.opspanel.system.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.dto.userrole.UserRoleAssignCmd;
import com.opspanel.system.dto.userrole.UserRoleQuery;
import com.opspanel.system.service.SysUserRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** REST controller for managing user-role relationships. */
@RestController
@RequestMapping("/api/system/user-role")
public class SysUserRoleController {

    private final SysUserRoleService userRoleService;

    public SysUserRoleController(SysUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    /** Assign multiple roles to a user. */
    @PostMapping("/assign")
    public ApiResponse<Boolean> assign(@RequestBody UserRoleAssignCmd cmd) {
        userRoleService.assignRoles(cmd.getUserId(), cmd.getRoleIds());
        return ApiResponse.ok(true);
    }

    /** Remove all roles of a user. */
    @DeleteMapping("/{userId}")
    public ApiResponse<Boolean> remove(@PathVariable Long userId) {
        userRoleService.removeRoles(userId);
        return ApiResponse.ok(true);
    }

    /** Retrieve roles assigned to a user. */
    @GetMapping("/list")
    public ApiResponse<List<Long>> list(UserRoleQuery query) {
        return ApiResponse.ok(userRoleService.getRoleIdsByUser(query.getUserId()));
    }
}
