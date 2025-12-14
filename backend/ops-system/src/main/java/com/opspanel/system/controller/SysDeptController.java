package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysDept;
import com.opspanel.system.dto.dept.DeptCreateCmd;
import com.opspanel.system.dto.dept.DeptQuery;
import com.opspanel.system.dto.dept.DeptUpdateCmd;
import com.opspanel.system.service.SysDeptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.ok(deptService.page(query, pageNum, pageSize));
    }

    /**
     * Create a new department.
     */
    @PostMapping("/create")
    public ApiResponse<Long> create(@RequestBody DeptCreateCmd cmd) {
        return ApiResponse.ok(deptService.create(cmd));
    }

    /**
     * Update an existing department.
     */
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody DeptUpdateCmd cmd) {
        return ApiResponse.ok(deptService.update(cmd));
    }

    /**
     * Delete departments by IDs.
     */
    @PostMapping("/batch-delete")
    public ApiResponse<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ApiResponse.ok(deptService.batchDelete(ids));
    }
}
