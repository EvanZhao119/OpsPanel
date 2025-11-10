package com.opspanel.system.listener;

import com.opspanel.framework.event.OperLogEvent;
import com.opspanel.system.domain.SysOperLog;
import com.opspanel.system.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for OperLogEvent and saves to database.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperLogListener {

    private final SysOperLogService logService;

    @EventListener
    public void handleOperLogEvent(OperLogEvent event) {
        SysOperLog logEntry = new SysOperLog();
        logEntry.setTitle(event.getTitle());
        logEntry.setMethod(event.getMethod());
        logEntry.setRequestUri(event.getRequestUri());
        logEntry.setRequestMethod(event.getRequestMethod());
        logEntry.setOperator(event.getOperator());
        logEntry.setOperTime(event.getOperTime());
        logEntry.setStatus(event.getStatus());
        logEntry.setErrorMsg(event.getErrorMsg());

        logService.record(logEntry);
    }
}
