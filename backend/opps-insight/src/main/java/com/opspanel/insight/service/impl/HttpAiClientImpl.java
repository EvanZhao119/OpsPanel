package com.opspanel.insight.service.impl;

import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.service.AiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP-based AI client implementation.
 * You can point this to your Python backend or an OpenAI proxy.
 */
@Service
public class HttpAiClientImpl implements AiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${insight.ai.endpoint:http://localhost:8081/ai/chat}")
    private String aiEndpoint;

    @Override
    public String chat(String prompt, List<InsightMessage> history, String contextJson) {

        Map<String, Object> body = new HashMap<>();
        body.put("prompt", prompt);
        body.put("history", history);
        body.put("context", contextJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> resp = restTemplate.exchange(
                aiEndpoint, HttpMethod.POST, entity, Map.class);

        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Object answer = resp.getBody().get("answer");
            return answer != null ? answer.toString() : "";
        }
        throw new RuntimeException("AI service request failed: " + resp.getStatusCode());
    }
}
