package com.opspanel.task.quartz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opspanel.task.domain.TaskJob;
import com.opspanel.task.mapper.TaskJobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Load all enabled jobs from database and register them into Quartz
 * when the application starts.
 *
 * Convention:
 *  - status = "0" : enabled (NORMAL)
 *  - status = "1" : paused
 *
 * After this component is added, any job with status "0" will be
 * scheduled automatically after the application has started.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskJobStartupLoader implements CommandLineRunner {

    private final TaskJobMapper taskJobMapper;
    private final JobScheduler jobScheduler;

    @Override
    public void run(String... args) {
        log.info("[TaskJobStartupLoader] Loading enabled jobs from database...");

        // Adjust "0" if your status code is different.
        List<TaskJob> jobs = taskJobMapper.selectList(
                new LambdaQueryWrapper<TaskJob>()
                        .eq(TaskJob::getStatus, "0")
        );

        if (jobs.isEmpty()) {
            log.info("[TaskJobStartupLoader] No enabled jobs found, nothing to schedule.");
            return;
        }

        log.info("[TaskJobStartupLoader] Found {} enabled jobs, scheduling them in Quartz...", jobs.size());

        for (TaskJob job : jobs) {
            try {
                jobScheduler.scheduleJob(job);
                log.info(
                        "[TaskJobStartupLoader] Scheduled job. id={}, name={}, group={}, type={}, cron={}",
                        job.getId(), job.getJobName(), job.getJobGroup(), job.getJobType(), job.getCronExpression()
                );
            } catch (Exception ex) {
                // Do not block other jobs because of a single failure
                log.error(
                        "[TaskJobStartupLoader] Failed to schedule job. id={}, name={}, group={}",
                        job.getId(), job.getJobName(), job.getJobGroup(), ex
                );
            }
        }

        log.info("[TaskJobStartupLoader] Finished scheduling enabled jobs.");
    }
}
