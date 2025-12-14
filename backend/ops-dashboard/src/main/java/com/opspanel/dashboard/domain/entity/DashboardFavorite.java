package com.opspanel.dashboard.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User dashboard favorite / homepage preference entity.
 */
@Data
@TableName("dashboard_favorite")
public class DashboardFavorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** User ID */
    private Long userId;

    /** Dashboard page ID */
    private Long pageId;

    /** 1 = set as homepage for this user */
    private Integer isHome;

    /** Created time */
    private LocalDateTime createTime;
}
