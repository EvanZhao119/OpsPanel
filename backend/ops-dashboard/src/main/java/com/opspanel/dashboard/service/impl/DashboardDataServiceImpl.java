package com.opspanel.dashboard.service.impl;

import com.opspanel.dashboard.domain.vo.DashboardDataVO;
import com.opspanel.dashboard.service.DashboardDataService;
import com.opspanel.task.service.ITaskJobService;
import com.opspanel.task.service.ITaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementation of dashboard data routing by data source code.
 */
@Service
public class DashboardDataServiceImpl implements DashboardDataService {

    @Autowired
    private ITaskJobService taskJobService;

    @Autowired
    private ITaskLogService taskLogService;

    @Override
    public DashboardDataVO queryByDataSource(String dataSource, Map<String, Object> params) {

        if (params == null) {
            // Avoid NullPointerException when accessing parameters
            params = Map.of();
        }

        switch (dataSource) {

            // Simple statistic: total number of scheduled tasks
            case "task.totalCount": {
                long total = taskJobService.countAllJobs();
                return DashboardDataVO.stat("Total Tasks", total);
            }

            // Simple statistic: failure rate in last 24 hours
            case "task.failRate24h": {
                double rate = taskLogService.calcFailRateLast24h();
                return DashboardDataVO.percentage("Failure Rate (24h)", rate);
            }

            // Time series: number of failed executions grouped by hour in last 24 hours
            case "task.failuresByHour24h": {
                Object series = taskLogService.countFailuresByHourLast24h();
                return DashboardDataVO.timeSeries("Failures by Hour (24h)", series);
            }

            // Table: top N failed jobs by failure count
            case "task.topFailedJobs": {
                int limit = 10;
                if (params.get("limit") instanceof Number) {
                    limit = ((Number) params.get("limit")).intValue();
                }
                Object rows = taskLogService.selectTopFailedJobs(limit);
                return DashboardDataVO.table("Top Failed Jobs", rows);
            }

            default:
                throw new RuntimeException("Unknown data source: " + dataSource);
        }
    }
}
