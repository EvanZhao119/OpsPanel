package com.opspanel.dashboard.service;

import com.opspanel.dashboard.domain.vo.DashboardDataVO;

import java.util.Map;

/**
 * Service for resolving widget data by data source code.
 */
public interface DashboardDataService {

    DashboardDataVO queryByDataSource(String dataSource, Map<String, Object> params);
}
