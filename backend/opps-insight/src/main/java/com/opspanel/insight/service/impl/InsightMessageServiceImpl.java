package com.opspanel.insight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.mapper.InsightMessageMapper;
import com.opspanel.insight.service.InsightMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InsightMessageServiceImpl
        extends ServiceImpl<InsightMessageMapper, InsightMessage>
        implements InsightMessageService {

    @Override
    public InsightMessage saveUserMessage(Long sessionId, String content) {
        InsightMessage msg = new InsightMessage();
        msg.setSessionId(sessionId);
        msg.setRole("USER");
        msg.setContent(content);
        msg.setCreateTime(LocalDateTime.now());
        save(msg);
        return msg;
    }

    @Override
    public InsightMessage saveAssistantMessage(Long sessionId, String content) {
        InsightMessage msg = new InsightMessage();
        msg.setSessionId(sessionId);
        msg.setRole("ASSISTANT");
        msg.setContent(content);
        msg.setCreateTime(LocalDateTime.now());
        save(msg);
        return msg;
    }

    @Override
    public List<InsightMessage> listBySession(Long sessionId) {
        return lambdaQuery()
                .eq(InsightMessage::getSessionId, sessionId)
                .orderByAsc(InsightMessage::getId)
                .list();
    }
}
