package com.opspanel.task.executor;

import com.opspanel.task.domain.TaskJob;

/**
 * Contract for executing a scheduled job.
 */
public interface TaskExecutor {

    /**
     * Execute the given scheduled job according to its type and invokeTarget.
     *
     * @param job scheduled job definition
     * @throws Exception if execution fails
     */
    void execute(TaskJob job) throws Exception;
}
