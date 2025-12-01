package com.opspanel.insight.service;

import com.opspanel.insight.domain.dto.InsightChatRequest;
import com.opspanel.insight.domain.vo.InsightChatResponse;

/**
 * Service for AI chat interaction.
 */
public interface InsightChatService {

    InsightChatResponse chat(InsightChatRequest request);
}
