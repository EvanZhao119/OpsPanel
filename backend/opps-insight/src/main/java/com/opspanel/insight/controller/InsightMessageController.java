package com.opspanel.insight.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.service.InsightMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for accessing insight chat messages.
 */
@RestController
@RequestMapping("/api/insight/message")
public class InsightMessageController {

    @Autowired
    private InsightMessageService insightMessageService;

    /**
     * List all messages of a session.
     *
     * @param sessionId session id
     */
    @GetMapping("/listBySession/{sessionId}")
    public ApiResponse<List<InsightMessage>> listBySession(
            @PathVariable("sessionId") Long sessionId) {

        List<InsightMessage> list = insightMessageService.listBySessionId(sessionId);
        return ApiResponse.ok(list);
    }
}
