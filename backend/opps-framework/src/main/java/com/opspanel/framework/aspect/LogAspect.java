package com.opspanel.framework.aspect;

import com.opspanel.framework.aspect.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method execution time.
 */
@Aspect
@Component
public class LogAspect {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(log)")
    public Object logExecution(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        logger.info("Executed %s in %d ms%n", joinPoint.getSignature(), duration);
        return result;
    }
}
