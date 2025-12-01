package com.opspanel.dashboard.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * Request DTO for querying dashboard data by data source.
 */
@Data
public class DashboardDataQueryDTO {

    /** Data source code of the widget, e.g., task.failRate24h */
    private String dataSource;

    /** Optional parameters: time range, filters, etc. */
    private Map<String, Object> params;
}
