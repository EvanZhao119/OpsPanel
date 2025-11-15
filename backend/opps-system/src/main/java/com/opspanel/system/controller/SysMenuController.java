package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysMenu;
import com.opspanel.system.dto.menu.MenuCreateCmd;
import com.opspanel.system.dto.menu.MenuQuery;
import com.opspanel.system.dto.menu.MenuUpdateCmd;
import com.opspanel.system.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for menu management.
 */
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {

    private final SysMenuService menuService;

    public SysMenuController(SysMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Retrieve a paginated list of menus.
     */
    @GetMapping("/list")
    public ApiResponse<IPage<SysMenu>> list(MenuQuery query,
                                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.ok(menuService.page(query, pageNum, pageSize));
    }

    /**
     * Create a new menu.
     */
    @PostMapping("/create")
    public ApiResponse<Long> create(@RequestBody MenuCreateCmd cmd) {
        return ApiResponse.ok(menuService.create(cmd));
    }

    /**
     * Update an existing menu.
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody MenuUpdateCmd cmd) {
        return ApiResponse.ok(menuService.update(cmd));
    }

    /**
     * Delete menus by IDs.
     */
    @PostMapping("/batch-delete")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ApiResponse.ok(menuService.batchDelete(ids));
    }
}
