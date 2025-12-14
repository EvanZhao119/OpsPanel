package com.opspanel.task.service;

import com.opspanel.task.domain.TaskLog;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing scheduled job execution logs.
 */
public interface ITaskLogService {

    /**
     * Find a log entry by its primary key.
     */
    TaskLog selectLogById(Long id);

    /**
     * Query log entries by conditions.
     */
    List<TaskLog> selectLogList(TaskLog query);

    /**
     * Insert a new log entry.
     */
    int insertLog(TaskLog log);

    /**
     * Delete all log entries.
     */
    int cleanLog();

    /**
     * Calculate failure rate for executions in the last 24 hours.
     * The result should be in range [0, 1], e.g. 0.25 means 25% failure rate.
     *
     * Implementation detail is up to the service layer:
     * for example, you can count all logs in last 24h and divide failed count by total.
     */
    double calcFailRateLast24h();

    /**
     * Count failed executions in the last 24 hours, grouped by hour.
     *
     * Recommended Map keys (you can adjust in your implementation):
     * - "time": hour label, e.g. "2025-11-30 10:00"
     * - "value": number of failed executions in this hour
     *
     * This method is used for time series charts on the dashboard.
     */
    List<Map<String, Object>> countFailuresByHourLast24h();

    /**
     * Select top N jobs by failure count based on execution logs.
     *
     * Each map element may contain fields like:
     * - "jobId"
     * - "jobName"
     * - "failCount"
     *
     * Exact structure is up to your implementation and the dashboard needs.
     */
    List<Map<String, Object>> selectTopFailedJobs(int limit);

}
