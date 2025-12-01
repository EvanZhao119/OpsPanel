package com.opspanel.dashboard.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.dashboard.domain.entity.DashboardWidget;
import com.opspanel.dashboard.service.DashboardWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for dashboard widget management.
 */
@RestController
@RequestMapping("/dashboard/widget")
public class DashboardWidgetController {

    @Autowired
    private DashboardWidgetService dashboardWidgetService;

    /**
     * Get widget by ID.
     */
    @GetMapping("/{id}")
    public ApiResponse<DashboardWidget> get(@PathVariable Long id) {
        DashboardWidget widget = dashboardWidgetService.getById(id);
        return ApiResponse.ok(widget);
    }

    /**
     * List widgets by dashboard page ID.
     */
    @GetMapping("/listByPage/{pageId}")
    public ApiResponse<List<DashboardWidget>> listByPage(@PathVariable Long pageId) {
        List<DashboardWidget> list = dashboardWidgetService.listByPageId(pageId);
        return ApiResponse.ok(list);
    }

    /**
     * Create a new widget.
     */
    @PostMapping
    public ApiResponse<Boolean> create(@RequestBody DashboardWidget widget) {
        widget.setDelFlag(0);
        boolean ok = dashboardWidgetService.save(widget);
        return ApiResponse.ok(ok);
    }

    /**
     * Update widget.
     */
    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody DashboardWidget widget) {
        boolean ok = dashboardWidgetService.updateById(widget);
        return ApiResponse.ok(ok);
    }

    /**
     * Soft delete widget.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        DashboardWidget widget = dashboardWidgetService.getById(id);
        if (widget == null) {
            return ApiResponse.error("Dashboard widget not found");
        }
        widget.setDelFlag(1);
        boolean ok = dashboardWidgetService.updateById(widget);
        return ApiResponse.ok(ok);
    }
}
