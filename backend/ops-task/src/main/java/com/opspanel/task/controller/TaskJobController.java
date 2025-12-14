package com.opspanel.task.controller;

import com.opspanel.common.api.ApiResponse;
import com.opspanel.task.domain.TaskJob;
import com.opspanel.task.service.ITaskJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller for managing scheduled jobs.
 */
@RestController
@RequestMapping("/api/task/job")
@RequiredArgsConstructor
public class TaskJobController {

    private final ITaskJobService jobService;

    /**
     * Get details of a single job.
     */
    @GetMapping("/{id}")
    public ApiResponse<TaskJob> get(@PathVariable("id") Long id) {
        TaskJob job = jobService.selectJobById(id);
        return ApiResponse.ok(job);
    }

    /**
     * Get a list of jobs by simple filter conditions.
     * You can pass query parameters like ?jobName=xxx&status=0.
     */
    @GetMapping("/list")
    public ApiResponse<List<TaskJob>> list(TaskJob query) {
        List<TaskJob> list = jobService.selectJobList(query);
        return ApiResponse.ok(list);
    }

    /**
     * Create a new job.
     */
    @PostMapping
    public ApiResponse<Boolean> add(@RequestBody TaskJob job) {
        int rows = jobService.createJob(job);
        return ApiResponse.ok(rows > 0);
    }

    /**
     * Update an existing job.
     */
    @PutMapping
    public ApiResponse<Boolean> edit(@RequestBody TaskJob job) {
        int rows = jobService.updateJob(job);
        return ApiResponse.ok(rows > 0);
    }

    /**
     * Change the status of a job (activate / pause).
     */
    @PutMapping("/{id}/status/{status}")
    public ApiResponse<Boolean> changeStatus(@PathVariable("id") Long id,
                                             @PathVariable("status") String status) {
        int rows = jobService.changeStatus(id, status);
        return ApiResponse.ok(rows > 0);
    }

    /**
     * Delete one or more jobs.
     * Example: DELETE /task/job/1,2,3
     */
    @DeleteMapping("/{ids}")
    public ApiResponse<Boolean> remove(@PathVariable("ids") Long[] ids) {
        int rows = jobService.deleteJobByIds(Arrays.asList(ids));
        return ApiResponse.ok(rows > 0);
    }

    /**
     * Trigger a job to run once immediately.
     */
    @PostMapping("/{id}/runOnce")
    public ApiResponse<Void> runOnce(@PathVariable("id") Long id) {
        jobService.runOnce(id);
        return ApiResponse.ok(null);
    }
}
