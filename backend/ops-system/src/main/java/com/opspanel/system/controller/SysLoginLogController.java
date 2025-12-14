package com.opspanel.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.common.api.ApiResponse;
import com.opspanel.system.domain.SysLoginLog;
import com.opspanel.system.dto.log.LoginLogQuery;
import com.opspanel.system.service.SysLoginLogService;
import org.springframework.web.bind.annotation.*;

/** REST controller for system login logs. */
@RestController
@RequestMapping("/api/system/login-log")
public class SysLoginLogController {

    private final SysLoginLogService logService;

    public SysLoginLogController(SysLoginLogService logService) {
        this.logService = logService;
    }

    /** Paginated query for login logs. */
    @GetMapping("/list")
    public ApiResponse<IPage<SysLoginLog>> list(LoginLogQuery query,
                                                @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.ok(logService.page(query, pageNum, pageSize));
    }
}
