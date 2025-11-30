package com.opspanel.task.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Scheduled job execution log entity.
 */
@Data
@TableName("task_log")
public class TaskLog {

    /** Primary key */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Reference to the job that was executed */
    private Long jobId;

    /** Job name at the time of execution */
    private String jobName;

    /** Job group at the time of execution */
    private String jobGroup;

    /** Job type at the time of execution */
    private String jobType;

    /** Target that was invoked */
    private String invokeTarget;

    /** Execution status: 0=success 1=failure */
    private String status;

    /** Execution start time */
    private LocalDateTime startTime;

    /** Execution end time */
    private LocalDateTime endTime;

    /** Execution time in milliseconds */
    private Long elapsedMs;

    /** Short message describing the execution result */
    private String jobMessage;

    /** Exception stack trace if the execution failed */
    private String exceptionInfo;

    /** Time when this log entry was created */
    private LocalDateTime createTime;
}
