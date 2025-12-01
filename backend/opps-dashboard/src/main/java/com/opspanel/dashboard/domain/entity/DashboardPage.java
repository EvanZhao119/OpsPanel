package com.opspanel.dashboard.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Dashboard page definition entity.
 */
@Data
@TableName("dashboard_page")
public class DashboardPage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Unique code of the dashboard page */
    private String pageCode;

    /** Display name of the dashboard page */
    private String pageName;

    /** Page type: SYSTEM or CUSTOM */
    private String pageType;

    /** Description of the dashboard */
    private String description;

    /** 1 = default page for the system */
    private Integer isDefault;

    /** 1 = enabled, 0 = disabled */
    private Integer status;

    /** Audit fields */
    private String creator;
    private LocalDateTime createTime;
    private String updater;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
