package com.opspanel.dashboard.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.dashboard.domain.entity.DashboardFavorite;
import com.opspanel.dashboard.service.DashboardFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user dashboard favorites (homepage settings).
 */
@RestController
@RequestMapping("/dashboard/favorite")
public class DashboardFavoriteController {

    @Autowired
    private DashboardFavoriteService dashboardFavoriteService;

    /**
     * List favorites for current user.
     * TODO: Replace hard-coded userId with actual current user.
     */
    @GetMapping("/list")
    public ApiResponse<List<DashboardFavorite>> listByUser() {
        Long userId = 0L; // TODO: get from security context
        List<DashboardFavorite> list = dashboardFavoriteService.listByUserId(userId);
        return ApiResponse.ok(list);
    }

    /**
     * Add a page to favorites for current user.
     */
    @PostMapping("/add/{pageId}")
    public ApiResponse<Boolean> addFavorite(@PathVariable Long pageId) {
        Long userId = 0L; // TODO: get from security context

        DashboardFavorite favorite = new DashboardFavorite();
        favorite.setUserId(userId);
        favorite.setPageId(pageId);
        favorite.setIsHome(0);
        boolean ok = dashboardFavoriteService.save(favorite);
        return ApiResponse.ok(ok);
    }

    /**
     * Set homepage dashboard for current user.
     */
    @PostMapping("/setHome/{pageId}")
    public ApiResponse<Void> setHome(@PathVariable Long pageId) {
        Long userId = 0L; // TODO: get from security context
        dashboardFavoriteService.setHomePage(userId, pageId);
        return ApiResponse.ok();
    }
}
