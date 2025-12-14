package com.opspanel.insight.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI insight chat session entity.
 */
@Data
@TableName("insight_session")
public class InsightSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Unique session code for external reference */
    private String sessionCode;

    /** Human-readable session title */
    private String title;

    /** Initiator user ID */
    private Long userId;

    /** Context source: DASHBOARD / TASK / SYSTEM / MANUAL */
    private String sourceType;

    /** Reference ID related to the source, e.g., dashboard page ID */
    private String sourceRef;

    /** 1 = active, 0 = closed */
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
