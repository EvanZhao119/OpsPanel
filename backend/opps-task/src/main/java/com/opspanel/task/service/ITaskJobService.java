package com.opspanel.task.service;

import com.opspanel.task.domain.TaskJob;

import java.util.List;

/**
 * Service interface for managing scheduled jobs.
 */
public interface ITaskJobService {

    /**
     * Find a job by its primary key.
     *
     * @param id job id
     * @return job entity or null if not found
     */
    TaskJob selectJobById(Long id);

    /**
     * Query jobs by simple conditions (name, group, status, etc.).
     * The fields of {@code query} are used as optional filters.
     *
     * @param query filter conditions
     * @return list of matching jobs
     */
    List<TaskJob> selectJobList(TaskJob query);

    /**
     * Create a new job.
     *
     * @param job job definition
     * @return number of rows affected
     */
    int createJob(TaskJob job);

    /**
     * Update an existing job by its id.
     *
     * @param job job definition with id set
     * @return number of rows affected
     */
    int updateJob(TaskJob job);

    /**
     * Change the status of a job (e.g. activate or pause).
     *
     * @param jobId  job id
     * @param status new status value
     * @return number of rows affected
     */
    int changeStatus(Long jobId, String status);

    /**
     * Delete one or more jobs by their ids.
     *
     * @param jobIds list of job ids
     * @return number of rows affected
     */
    int deleteJobByIds(List<Long> jobIds);

    /**
     * Trigger a job to run once immediately.
     * This is a stub in the minimal version and can be wired to a real scheduler later.
     *
     * @param jobId job id
     */
    void runOnce(Long jobId);

    /**
     * Count all jobs in the system.
     *
     * Implementation detail is up to the service layer:
     * for example, you can count records in the job table,
     * optionally excluding logically deleted ones.
     */
    int countAllJobs();
}
