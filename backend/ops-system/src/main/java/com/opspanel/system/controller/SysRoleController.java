package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysRole;
import com.opspanel.system.dto.role.RoleCreateCmd;
import com.opspanel.system.dto.role.RoleQuery;
import com.opspanel.system.dto.role.RoleUpdateCmd;
import com.opspanel.system.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.ok(roleService.page(query, pageNum, pageSize));
    }

    @PostMapping("/create")
    public ApiResponse<Long> create(@RequestBody RoleCreateCmd cmd) {
        return ApiResponse.ok(roleService.create(cmd));
    }

    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody RoleUpdateCmd cmd) {
        return ApiResponse.ok(roleService.update(cmd));
    }

    @PostMapping("/batch-delete")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ApiResponse.ok(roleService.batchRemove(ids));
    }
}
