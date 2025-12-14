package com.opspanel.dashboard.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opspanel.dashboard.domain.entity.DashboardWidget;

import java.util.List;

public interface DashboardWidgetService extends IService<DashboardWidget> {

    /**
     * List all widgets under a dashboard page.
     */
    List<DashboardWidget> listByPageId(Long pageId);
}
