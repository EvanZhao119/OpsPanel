package com.opspanel.insight.domain.dto;

import lombok.Data;

/**
 * Request DTO for AI chat.
 */
@Data
public class InsightChatRequest {

    /** Optional existing session ID; null = create new session */
    private Long sessionId;

    /** Source type, e.g., DASHBOARD / TASK / SYSTEM / MANUAL */
    private String sourceType;

    /** Source reference, e.g., dashboard page ID */
    private String sourceRef;

    /** User question to ask the AI assistant */
    private String question;

    /**
     * Optional JSON string containing contextual data snapshot,
     * e.g., dashboard metrics for the current page.
     */
    private String contextJson;
}
