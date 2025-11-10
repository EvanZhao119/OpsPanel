package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysRole;
import com.opspanel.system.dto.role.RoleCreateCmd;
import com.opspanel.system.dto.role.RoleQuery;
import com.opspanel.system.dto.role.RoleUpdateCmd;
import com.opspanel.system.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

/** REST controller for role management. */
@RestController
@RequestMapping("/api/system/role")
public class SysRoleController {

    private final SysRoleService roleService;

    public SysRoleController(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ApiResponse<IPage<SysRole>> list(RoleQuery query,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.ok(roleService.page(query, pageNum, pageSize));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody RoleCreateCmd cmd) {
        return ApiResponse.ok(roleService.create(cmd));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody RoleUpdateCmd cmd) {
        return ApiResponse.ok(roleService.update(cmd));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.ok(roleService.remove(id));
    }
}
