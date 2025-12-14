package com.opspanel.insight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.insight.domain.dto.InsightChatRequest;
import com.opspanel.insight.domain.entity.InsightSession;
import com.opspanel.insight.mapper.InsightSessionMapper;
import com.opspanel.insight.service.InsightSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InsightSessionServiceImpl
        extends ServiceImpl<InsightSessionMapper, InsightSession>
        implements InsightSessionService {

    @Override
    public InsightSession getOrCreate(InsightChatRequest request) {
        if (request.getSessionId() != null) {
            InsightSession existing = getById(request.getSessionId());
            if (existing != null) {
                return existing;
            }
        }

        // Create new session
        InsightSession session = new InsightSession();
        session.setSessionCode(UUID.randomUUID().toString().replace("-", ""));
        session.setTitle("Insight Session");
        // TODO: Replace with real current user ID if available
        session.setUserId(0L);
        session.setSourceType(request.getSourceType());
        session.setSourceRef(request.getSourceRef());
        session.setStatus(1);
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());

        save(session);
        return session;
    }
}
