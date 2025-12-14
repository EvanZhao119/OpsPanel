package com.opspanel.framework.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * RequestLoggingFilter
 *
 * <p>This filter logs every incoming HTTP request with a unique request ID and
 * optional user context. It uses the MDC (Mapped Diagnostic Context) mechanism
 * to attach request-level information (such as requestId and userId) to log entries,
 * which helps in end-to-end traceability and debugging.</p>
 *
 * <p>Typical log example:</p>
 * <pre>
 * [1b23fa9e-4567-890a] GET /api/users (32 ms)
 * </pre>
 *
 * @author OpsPanel
 */
@Slf4j
@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;

        // Generate a unique ID for this request (useful for tracing across logs)
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        // Optionally capture user identity from custom headers or JWT
        String user = httpReq.getHeader("X-User") != null ? httpReq.getHeader("X-User") : "anonymous";
        MDC.put("userId", user);

        long start = System.currentTimeMillis();
        try {
            // Proceed with the filter chain
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;

            // Log request method, URI, and execution time
            log.info("[{}] {} {} ({} ms)", requestId, httpReq.getMethod(), httpReq.getRequestURI(), duration);

            // Clear MDC to avoid data leakage across threads
            MDC.clear();
        }
    }
}
