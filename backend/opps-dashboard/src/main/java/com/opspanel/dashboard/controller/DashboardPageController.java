package com.opspanel.dashboard.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.dashboard.domain.entity.DashboardPage;
import com.opspanel.dashboard.service.DashboardPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for dashboard page management.
 */
@RestController
@RequestMapping("/api/dashboard/page")
public class DashboardPageController {

    @Autowired
    private DashboardPageService dashboardPageService;

    /**
     * Get dashboard page by ID.
     */
    @GetMapping("/{id}")
    public ApiResponse<DashboardPage> get(@PathVariable Long id) {
        DashboardPage page = dashboardPageService.getById(id);
        return ApiResponse.ok(page);
    }

    /**
     * List all dashboard pages.
     * You can add filters (status, type, etc.) later if needed.
     */
    @GetMapping("/list")
    public ApiResponse<List<DashboardPage>> list() {
        List<DashboardPage> list = dashboardPageService.lambdaQuery()
                .eq(DashboardPage::getDelFlag, 0)
                .list();
        return ApiResponse.ok(list);
    }

    /**
     * Create a new dashboard page.
     */
    @PostMapping
    public ApiResponse<Boolean> create(@RequestBody DashboardPage page) {
        page.setDelFlag(0);
        boolean ok = dashboardPageService.save(page);
        return ApiResponse.ok(ok);
    }

    /**
     * Update an existing dashboard page.
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody DashboardPage page) {
        boolean ok = dashboardPageService.updateById(page);
        return ApiResponse.ok(ok);
    }

    /**
     * Soft delete a dashboard page.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        DashboardPage page = dashboardPageService.getById(id);
        if (page == null) {
            return ApiResponse.error("Dashboard page not found");
        }
        page.setDelFlag(1);
        boolean ok = dashboardPageService.updateById(page);
        return ApiResponse.ok(ok);
    }
}
