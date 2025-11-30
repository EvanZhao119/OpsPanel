package com.opspanel.task.service;

import com.opspanel.task.domain.TaskLog;

import java.util.List;

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
}
