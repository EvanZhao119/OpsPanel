package com.opspanel.insight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opspanel.insight.domain.entity.InsightMessage;

import java.util.List;

public interface InsightMessageService extends IService<InsightMessage> {

    InsightMessage saveUserMessage(Long sessionId, String content);

    InsightMessage saveAssistantMessage(Long sessionId, String content);

    /**
     * List all messages of a session in chronological order.
     *
     * @param sessionId session id
     * @return message list, empty if no record or sessionId is null
     */
    List<InsightMessage> listBySessionId(Long sessionId);
}
