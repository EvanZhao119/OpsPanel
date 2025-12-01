package com.opspanel.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opspanel.dashboard.domain.entity.DashboardFavorite;

import java.util.List;

public interface DashboardFavoriteService extends IService<DashboardFavorite> {

    /**
     * List favorites for a specific user.
     */
    List<DashboardFavorite> listByUserId(Long userId);

    /**
     * Set homepage dashboard for a user.
     */
    void setHomePage(Long userId, Long pageId);
}
