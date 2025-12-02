package com.opspanel.insight.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.insight.domain.entity.InsightSession;
import com.opspanel.insight.service.InsightSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing AI insight chat sessions.
 */
@RestController
@RequestMapping("/api/insight/session")
public class InsightSessionController {

    @Autowired
    private InsightSessionService insightSessionService;

    /**
     * Get details of a single session by ID.
     */
    @GetMapping("/{id}")
    public ApiResponse<InsightSession> get(@PathVariable Long id) {
        InsightSession session = insightSessionService.getById(id);
        return ApiResponse.ok(session);
    }

    /**
     * List all sessions of current user.
     * TODO: Replace hard-coded userId with real current user.
     */
    @GetMapping("/list")
    public ApiResponse<List<InsightSession>> list() {
        // For now, list all sessions; you can filter by userId later
        List<InsightSession> list = insightSessionService.list();
        return ApiResponse.ok(list);
    }

    /**
     * Close a session.
     */
    @PostMapping("/{id}/close")
    public ApiResponse<Void> close(@PathVariable Long id) {
        InsightSession session = insightSessionService.getById(id);
        if (session == null) {
            return ApiResponse.error("Session not found");
        }
        session.setStatus(0);
        insightSessionService.updateById(session);
        return ApiResponse.ok();
    }
}
