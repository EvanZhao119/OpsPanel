package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysDept;
import com.opspanel.system.dto.dept.DeptCreateCmd;
import com.opspanel.system.dto.dept.DeptQuery;
import com.opspanel.system.dto.dept.DeptUpdateCmd;
import com.opspanel.system.service.SysDeptService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for department management.
 */
@RestController
@RequestMapping("/api/system/dept")
public class SysDeptController {

    private final SysDeptService deptService;

    public SysDeptController(SysDeptService deptService) {
        this.deptService = deptService;
    }

    /**
     * Retrieve a paginated list of departments.
     */
    @GetMapping("/list")
    public ApiResponse<IPage<SysDept>> list(DeptQuery query,
                                            @RequestParam(defaultValue = "1") int pageNum,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.ok(deptService.page(query, pageNum, pageSize));
    }

    /**
     * Create a new department.
     */
    @PostMapping
    public ApiResponse<Long> create(@RequestBody DeptCreateCmd cmd) {
        return ApiResponse.ok(deptService.create(cmd));
    }

    /**
     * Update an existing department.
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody DeptUpdateCmd cmd) {
        return ApiResponse.ok(deptService.update(cmd));
    }

    /**
     * Delete a department by ID.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.ok(deptService.remove(id));
    }
}
