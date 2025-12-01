package com.opspanel.insight.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.insight.domain.dto.InsightChatRequest;
import com.opspanel.insight.domain.vo.InsightChatResponse;
import com.opspanel.insight.service.InsightChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for AI insight chat.
 */
@RestController
@RequestMapping("/insight/chat")
public class InsightChatController {

    @Autowired
    private InsightChatService insightChatService;

    @PostMapping
    public ApiResponse<InsightChatResponse> chat(@RequestBody InsightChatRequest request) {
        InsightChatResponse response = insightChatService.chat(request);
        return ApiResponse.ok(response);
    }
}
