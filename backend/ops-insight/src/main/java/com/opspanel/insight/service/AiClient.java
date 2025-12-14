package com.opspanel.insight.service;

import com.opspanel.insight.domain.entity.InsightMessage;

import java.util.List;

/**
 * Pluggable AI client interface.
 * You can implement this using OpenAI, local LLM, or your Python service.
 */
public interface AiClient {

    /**
     * Send a chat request to the AI model.
     *
     * @param prompt      Final prompt text constructed by the backend
     * @param history     Conversation history
     * @param contextJson Data snapshot in JSON form
     * @return AI generated answer text
     */
    String chat(String prompt, List<InsightMessage> history, String contextJson);
}
