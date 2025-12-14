package com.opspanel.insight.domain.vo;

import com.opspanel.insight.domain.entity.InsightMessage;
import com.opspanel.insight.domain.entity.InsightSession;
import lombok.Data;

/**
 * Response VO for AI chat.
 */
@Data
public class InsightChatResponse {

    /** Session info */
    private Long sessionId;

    /** Unique session code */
    private String sessionCode;

    /** Latest assistant message */
    private String answer;

    public static InsightChatResponse of(InsightSession session, InsightMessage answerMessage) {
        InsightChatResponse vo = new InsightChatResponse();
        vo.setSessionId(session.getId());
        vo.setSessionCode(session.getSessionCode());
        vo.setAnswer(answerMessage.getContent());
        return vo;
    }
}
