package com.opspanel.insight.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Prompt template entity for AI instructions.
 */
@Data
@TableName("insight_prompt_template")
public class InsightPromptTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Template code, e.g., DASHBOARD_SUMMARY */
    private String code;

    /** Template description */
    private String description;

    /** Prompt content with placeholders, e.g., {{metricJson}} */
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
