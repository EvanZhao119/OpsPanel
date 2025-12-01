package com.opspanel.insight.service.impl;

import com.opspanel.insight.domain.dto.InsightChatRequest;
import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.domain.entity.InsightSession;
import com.opspanel.insight.domain.vo.InsightChatResponse;
import com.opspanel.insight.service.AiClient;
import com.opspanel.insight.service.InsightChatService;
import com.opspanel.insight.service.InsightMessageService;
import com.opspanel.insight.service.InsightSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsightChatServiceImpl implements InsightChatService {

    @Autowired
    private InsightSessionService sessionService;

    @Autowired
    private InsightMessageService messageService;

    @Autowired
    private AiClient aiClient;

    @Override
    public InsightChatResponse chat(InsightChatRequest request) {

        // 1. Get or create session
        InsightSession session = sessionService.getOrCreate(request);

        // 2. Save user message
        messageService.saveUserMessage(session.getId(), request.getQuestion());

        // 3. Load history
        List<InsightMessage> history = messageService.listBySession(session.getId());

        // 4. Build prompt (can be improved using prompt templates)
        String prompt = buildPrompt(history, request.getContextJson(), request.getQuestion());

        // 5. Call AI client
        String answer = aiClient.chat(prompt, history, request.getContextJson());

        // 6. Save assistant message
        InsightMessage aiMsg = messageService.saveAssistantMessage(session.getId(), answer);

        // 7. Build response
        return InsightChatResponse.of(session, aiMsg);
    }

    private String buildPrompt(List<InsightMessage> history, String contextJson, String question) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an operations insight assistant. ")
                .append("Use the provided JSON context and conversation history ")
                .append("to analyze system metrics and provide concise, actionable insights.\n\n");

        if (contextJson != null && !contextJson.isEmpty()) {
            sb.append("Context JSON:\n").append(contextJson).append("\n\n");
        }

        sb.append("Current user question:\n").append(question).append("\n");
        return sb.toString();
    }
}
