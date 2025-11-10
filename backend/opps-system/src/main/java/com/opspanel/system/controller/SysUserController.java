package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.dto.user.UserCreateCmd;
import com.opspanel.system.dto.user.UserQuery;
import com.opspanel.system.dto.user.UserUpdateCmd;
import com.opspanel.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/user")
public class SysUserController {

    private final SysUserService userService;

    public SysUserController(SysUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ApiResponse<IPage<SysUser>> list(UserQuery q,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.ok(userService.page(q, pageNum, pageSize));
    }

    @PostMapping
    public ApiResponse<Long> create(@RequestBody UserCreateCmd cmd) {
        return ApiResponse.ok(userService.create(cmd));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody UserUpdateCmd cmd) {
        return ApiResponse.ok(userService.update(cmd));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.ok(userService.remove(id));
    }
}
