package com.opspanel.task.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Scheduled job entity.
 */
@Data
@TableName("task_job")
public class TaskJob {

    /** Primary key */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** Human-readable name of the job */
    private String jobName;

    /** Logical group of the job */
    private String jobGroup;

    /** Job type: JAVA / HTTP / AGENT etc. */
    private String jobType;

    /**
     * Target to invoke:
     *  - JAVA: beanName.methodName(arg1,arg2)
     *  - HTTP: GET:https://api.example.com/path
     *  - AGENT: agentKey:daily_report
     */
    private String invokeTarget;

    /** Cron expression defining when this job should run */
    private String cronExpression;

    /** Misfire policy: 1=default 2=fire now 3=ignore misfire */
    private String misfirePolicy;

    /** Whether concurrent executions are allowed: 0=no 1=yes */
    private String concurrent;

    /** Job status: 0=active 1=paused */
    private String status;

    /** Optional remark */
    private String remark;

    /** Username of the creator */
    private String createBy;

    /** Creation timestamp */
    private LocalDateTime createTime;

    /** Username of the last editor */
    private String updateBy;

    /** Last update timestamp */
    private LocalDateTime updateTime;
}
