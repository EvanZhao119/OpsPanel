package com.opspanel.insight.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI insight chat message entity.
 */
@Data
@TableName("insight_message")
public class InsightMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** Related session ID */
    private Long sessionId;

    /** Message role: USER / ASSISTANT / SYSTEM */
    private String role;

    /** Text content of the message */
    private String content;

    /** Optional metadata in JSON string, such as token usage, data sources, etc. */
    private String metaJson;

    private LocalDateTime createTime;
}
