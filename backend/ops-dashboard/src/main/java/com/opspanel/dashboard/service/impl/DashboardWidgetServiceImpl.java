package com.opspanel.dashboard.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.dashboard.domain.entity.DashboardWidget;
import com.opspanel.dashboard.mapper.DashboardWidgetMapper;
import com.opspanel.dashboard.service.DashboardWidgetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardWidgetServiceImpl
        extends ServiceImpl<DashboardWidgetMapper, DashboardWidget>
        implements DashboardWidgetService {

    @Override
    public List<DashboardWidget> listByPageId(Long pageId) {
        return lambdaQuery()
                .eq(DashboardWidget::getPageId, pageId)
                .eq(DashboardWidget::getDelFlag, 0)
                .orderByAsc(DashboardWidget::getSortOrder)
                .list();
    }
}
