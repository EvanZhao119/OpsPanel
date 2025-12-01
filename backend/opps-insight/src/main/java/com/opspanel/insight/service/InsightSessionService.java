package com.opspanel.insight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opspanel.insight.domain.entity.InsightSession;
import com.opspanel.insight.domain.dto.InsightChatRequest;

public interface InsightSessionService extends IService<InsightSession> {

    /**
     * Get existing session or create a new one based on the request.
     */
    InsightSession getOrCreate(InsightChatRequest request);
}
