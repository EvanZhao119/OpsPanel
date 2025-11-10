package com.opspanel.framework.aspect;

import com.opspanel.framework.aspect.annotation.LogOperation;
import com.opspanel.framework.event.OperLogEvent;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Aspect for automatically recording operation logs.
 * Captures request context and saves to sys_oper_log table.
 */
@Slf4j
@Aspect
@Component
public class OperLogAspect {

    private final ApplicationEventPublisher publisher;

    public OperLogAspect(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Around("@annotation(com.opspanel.framework.aspect.annotation.LogOperation)")
    public Object recordOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime now = LocalDateTime.now();
        OperLogEvent event = new OperLogEvent("", "", "", "", "", now, 1, null);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation annotation = method.getAnnotation(LogOperation.class);
        event.setTitle(annotation.title());
        event.setMethod(method.getDeclaringClass().getName() + "." + method.getName());
        event.setStatus(1);
        event.setOperTime(now);

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            event.setRequestUri(request.getRequestURI());
            event.setRequestMethod(request.getMethod());
            event.setOperator(annotation.operator());
        }

        try {
            Object result = joinPoint.proceed();
            event.setStatus(1);
            publisher.publishEvent(event);
            return result;
        } catch (Exception ex) {
            event.setStatus(0);
            event.setErrorMsg(ex.getMessage());
            publisher.publishEvent(event);
            throw ex;
        }
    }
}
