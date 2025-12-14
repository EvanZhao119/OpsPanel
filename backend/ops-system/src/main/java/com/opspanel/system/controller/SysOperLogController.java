package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysOperLog;
import com.opspanel.system.dto.log.OperLogQuery;
import com.opspanel.system.service.SysOperLogService;
import org.springframework.web.bind.annotation.*;

/** REST controller for system operation logs. */
@RestController
@RequestMapping("/api/system/oper-log")
public class SysOperLogController {

    private final SysOperLogService logService;

    public SysOperLogController(SysOperLogService logService) {
        this.logService = logService;
    }

    /** Paginated query for operation logs. */
    @GetMapping("/list")
    public ApiResponse<IPage<SysOperLog>> list(OperLogQuery query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.ok(logService.page(query, pageNum, pageSize));
    }
}
