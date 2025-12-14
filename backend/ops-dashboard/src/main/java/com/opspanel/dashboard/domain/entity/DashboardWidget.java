package com.opspanel.dashboard.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Dashboard widget entity.
 */
@Data
@TableName("dashboard_widget")
public class DashboardWidget {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Dashboard page ID this widget belongs to */
    private Long pageId;

    /** Widget type: STAT / CHART / TABLE / CUSTOM */
    private String widgetType;

    /** Widget title displayed on frontend */
    private String title;

    /** Grid X position */
    private Integer positionX;

    /** Grid Y position */
    private Integer positionY;

    /** Grid width */
    private Integer width;

    /** Grid height */
    private Integer height;

    /** Data source code, e.g., task.failRate24h */
    private String dataSource;

    /** Frontend render config in JSON string */
    private String configJson;

    /** Sort order within the dashboard */
    private Integer sortOrder;

    /** 1 = enabled, 0 = disabled */
    private Integer status;

    /** Audit fields */
    private String creator;
    private LocalDateTime createTime;
    private String updater;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
