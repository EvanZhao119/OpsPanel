package com.opspanel.insight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.mapper.InsightMessageMapper;
import com.opspanel.insight.service.InsightMessageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class InsightMessageServiceImpl
        extends ServiceImpl<InsightMessageMapper, InsightMessage>
        implements InsightMessageService {

    @Autowired
    private InsightMessageMapper insightMessageMapper;

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
    public List<InsightMessage> listBySessionId(Long sessionId) {
        if (sessionId == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<InsightMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InsightMessage::getSessionId, sessionId)
                // you can change to createTime if your entity has that field
                .orderByAsc(InsightMessage::getId);

        return insightMessageMapper.selectList(wrapper);
    }
}
