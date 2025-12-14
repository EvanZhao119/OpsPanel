package com.opspanel.dashboard.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.dashboard.domain.dto.DashboardDataQueryDTO;
import com.opspanel.dashboard.domain.vo.DashboardDataVO;
import com.opspanel.dashboard.service.DashboardDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for dashboard widget data queries.
 */
@RestController
@RequestMapping("/api/dashboard/data")
public class DashboardDataController {

    @Autowired
    private DashboardDataService dashboardDataService;

    @PostMapping("/query")
    public ApiResponse<DashboardDataVO> query(@RequestBody DashboardDataQueryDTO dto) {
        DashboardDataVO vo = dashboardDataService.queryByDataSource(
                dto.getDataSource(), dto.getParams());
        return ApiResponse.ok(vo);
    }
}
