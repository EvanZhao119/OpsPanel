package com.opspanel.task.quartz;

import com.opspanel.task.domain.TaskJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * Helper component that manages Quartz scheduling for OppsTaskJob.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final Scheduler scheduler;

    /**
     * Create a new Quartz job and schedule it with the given cron expression.
     */
    public void scheduleJob(TaskJob job) {
        try {
            JobDetail jobDetail = buildJobDetail(job);
            Trigger trigger = buildTrigger(job);
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Scheduled Quartz job: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule job " + job.getId(), e);
        }
    }

    /**
     * Update existing Quartz job's trigger when job definition changes.
     */
    public void rescheduleJob(TaskJob job) {
        try {
            TriggerKey triggerKey = getTriggerKey(job);
            Trigger newTrigger = buildTrigger(job);
            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("Rescheduled Quartz job: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to reschedule job " + job.getId(), e);
        }
    }

    /**
     * Delete Quartz job when it is removed from DB.
     */
    public void deleteJob(TaskJob job) {
        try {
            JobKey jobKey = getJobKey(job);
            scheduler.deleteJob(jobKey);
            log.info("Deleted Quartz job: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to delete job " + job.getId(), e);
        }
    }

    /**
     * Pause Quartz job (do not fire triggers until resumed).
     */
    public void pauseJob(TaskJob job) {
        try {
            scheduler.pauseJob(getJobKey(job));
            log.info("Paused Quartz job: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to pause job " + job.getId(), e);
        }
    }

    /**
     * Resume a previously paused Quartz job.
     */
    public void resumeJob(TaskJob job) {
        try {
            scheduler.resumeJob(getJobKey(job));
            log.info("Resumed Quartz job: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to resume job " + job.getId(), e);
        }
    }

    /**
     * Trigger a job to run once immediately.
     */
    public void triggerOnce(TaskJob job) {
        try {
            scheduler.triggerJob(getJobKey(job));
            log.info("Triggered Quartz job once: id={}, name={}", job.getId(), job.getJobName());
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to trigger job " + job.getId(), e);
        }
    }

    // ===== internal helpers =====

    private JobKey getJobKey(TaskJob job) {
        // Use job id as Quartz job name to guarantee uniqueness
        return JobKey.jobKey(String.valueOf(job.getId()), job.getJobGroup());
    }

    private TriggerKey getTriggerKey(TaskJob job) {
        return TriggerKey.triggerKey("TRIGGER_" + job.getId(), job.getJobGroup());
    }

    private JobDetail buildJobDetail(TaskJob job) {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("jobId", job.getId());
        dataMap.put("invokeTarget", job.getInvokeTarget());

        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity(getJobKey(job))
                .withDescription(job.getJobName())
                .usingJobData(dataMap)
                .storeDurably(false)
                .build();
    }

    private Trigger buildTrigger(TaskJob job) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

        // Apply simple misfire policy mapping
        switch (job.getMisfirePolicy()) {
            case "2": // fire now
                scheduleBuilder = scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                break;
            case "3": // ignore misfires
                scheduleBuilder = scheduleBuilder.withMisfireHandlingInstructionDoNothing();
                break;
            default:  // use Quartz default behaviour
        }

        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(job))
                .withSchedule(scheduleBuilder)
                .forJob(getJobKey(job))
                .build();
    }
}
