package com.opspanel.task.quartz;

import com.opspanel.task.domain.TaskJob;
import com.opspanel.task.executor.TaskExecutor;
import com.opspanel.task.service.ITaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Quartz job that fetches the job definition from DB and delegates execution
 * to {@link TaskExecutor}.
 */
@Slf4j
public class QuartzJob extends QuartzJobBean {

    @Autowired
    private ITaskJobService jobService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Long jobId = dataMap.getLong("jobId");

        TaskJob job = jobService.selectJobById(jobId);
        if (job == null) {
            log.warn("Scheduled job {} not found, skip execution", jobId);
            return;
        }

        long start = System.currentTimeMillis();
        try {
            log.info("Start executing job: id={}, name={}, type={}, target={}",
                    job.getId(), job.getJobName(), job.getJobType(), job.getInvokeTarget());

            taskExecutor.execute(job);

            long cost = System.currentTimeMillis() - start;
            log.info("Job executed successfully. jobId={}, costMs={}", job.getId(), cost);
        } catch (Exception ex) {
            long cost = System.currentTimeMillis() - start;
            log.error("Job execution failed. jobId={}, costMs={}", job.getId(), cost, ex);
            // later you can also log into opps_task_log table here
        }
    }
}
