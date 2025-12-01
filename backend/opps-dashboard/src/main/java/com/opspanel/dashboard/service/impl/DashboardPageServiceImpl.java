package com.opspanel.dashboard.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.dashboard.domain.entity.DashboardPage;
import com.opspanel.dashboard.mapper.DashboardPageMapper;
import com.opspanel.dashboard.service.DashboardPageService;
import org.springframework.stereotype.Service;

/**
 * Implementation of dashboard page service.
 */
@Service
public class DashboardPageServiceImpl
        extends ServiceImpl<DashboardPageMapper, DashboardPage>
        implements DashboardPageService {
    // For now, we use default CRUD methods from ServiceImpl.
    // You can add custom business methods here later.
}
