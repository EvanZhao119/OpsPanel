package com.opspanel.task.executor;

import com.opspanel.task.domain.TaskJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

/**
 * Default implementation of {@link TaskExecutor}.
 *
 * Supports:
 *  - JAVA: invoke a no-arg method on a Spring bean, using "beanName.methodName"
 *  - HTTP: fire a simple HTTP request using "METHOD:URL"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTaskExecutor implements TaskExecutor {

    private final ApplicationContext applicationContext;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void execute(TaskJob job) throws Exception {
        if (job == null) {
            throw new IllegalArgumentException("Job must not be null");
        }
        String jobType = job.getJobType();
        if (!StringUtils.hasText(jobType)) {
            jobType = "JAVA";
        }

        switch (jobType.toUpperCase()) {
            case "JAVA":
                executeJavaJob(job);
                break;
            case "HTTP":
                executeHttpJob(job);
                break;
            case "AGENT":
                // Placeholder for future AI agent integration
                log.warn("AGENT job type is not implemented yet. jobId={}", job.getId());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported jobType: " + jobType);
        }
    }

    /**
     * Execute a JAVA type job.
     * Expected invokeTarget format: "beanName.methodName"
     */
    protected void executeJavaJob(TaskJob job) throws Exception {
        String invokeTarget = job.getInvokeTarget();
        if (!StringUtils.hasText(invokeTarget) || !invokeTarget.contains(".")) {
            throw new IllegalArgumentException("Invalid invokeTarget for JAVA job: " + invokeTarget);
        }

        String beanName = invokeTarget.substring(0, invokeTarget.lastIndexOf('.'));
        String methodName = invokeTarget.substring(invokeTarget.lastIndexOf('.') + 1);

        Object bean = applicationContext.getBean(beanName);
        Method method = bean.getClass().getMethod(methodName);

        log.info("Invoking JAVA job. jobId={}, bean={}, method={}",
                job.getId(), beanName, methodName);

        method.invoke(bean);
    }

    /**
     * Execute an HTTP type job.
     * Expected invokeTarget format: "METHOD:URL", for example "GET:https://example.com/api/ping".
     */
    protected void executeHttpJob(TaskJob job) {
        String invokeTarget = job.getInvokeTarget();
        if (!StringUtils.hasText(invokeTarget) || !invokeTarget.contains(":")) {
            throw new IllegalArgumentException("Invalid invokeTarget for HTTP job: " + invokeTarget);
        }

        int idx = invokeTarget.indexOf(':');
        String method = invokeTarget.substring(0, idx).trim().toUpperCase();
        String url = invokeTarget.substring(idx + 1).trim();

        // Use valueOf() instead of resolve() so it works on all Spring versions
        HttpMethod httpMethod;
        try {
            httpMethod = HttpMethod.valueOf(method);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method, ex);
        }

        log.info("Invoking HTTP job. jobId={}, method={}, url={}",
                job.getId(), method, url);

        // Minimal request without body and headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response =
                restTemplate.exchange(url, httpMethod, requestEntity, String.class);

        log.info("HTTP job completed. jobId={}, statusCode={}, bodySnippet={}",
                job.getId(),
                response.getStatusCodeValue(),
                response.getBody() != null ? abbreviate(response.getBody(), 200) : null);
    }


    private String abbreviate(String text, int maxLen) {
        if (text == null || text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "...";
    }
}
