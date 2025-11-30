package com.opspanel.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.opspanel.task.domain.TaskJob;
import com.opspanel.task.mapper.TaskJobMapper;
import com.opspanel.task.quartz.JobScheduler;
import com.opspanel.task.service.ITaskJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Default implementation of {@link ITaskJobService}.
 * This version persists jobs to DB and wires them into Quartz.
 */
@Service
@RequiredArgsConstructor
public class TaskJobServiceImpl implements ITaskJobService {

    private final TaskJobMapper jobMapper;
    private final JobScheduler jobScheduler;

    @Override
    public TaskJob selectJobById(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    public List<TaskJob> selectJobList(TaskJob query) {
        LambdaQueryWrapper<TaskJob> wrapper = new LambdaQueryWrapper<>();

        if (query != null) {
            if (StringUtils.hasText(query.getJobName())) {
                wrapper.like(TaskJob::getJobName, query.getJobName());
            }
            if (StringUtils.hasText(query.getJobGroup())) {
                wrapper.eq(TaskJob::getJobGroup, query.getJobGroup());
            }
            if (StringUtils.hasText(query.getStatus())) {
                wrapper.eq(TaskJob::getStatus, query.getStatus());
            }
            if (StringUtils.hasText(query.getJobType())) {
                wrapper.eq(TaskJob::getJobType, query.getJobType());
            }
        }

        wrapper.orderByDesc(TaskJob::getCreateTime);
        return jobMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public int createJob(TaskJob job) {
        // Set safe defaults if fields are missing
        if (!StringUtils.hasText(job.getJobGroup())) {
            job.setJobGroup("DEFAULT");
        }
        if (!StringUtils.hasText(job.getJobType())) {
            job.setJobType("JAVA");
        }
        if (!StringUtils.hasText(job.getMisfirePolicy())) {
            job.setMisfirePolicy("1");
        }
        if (!StringUtils.hasText(job.getConcurrent())) {
            job.setConcurrent("1");
        }
        if (!StringUtils.hasText(job.getStatus())) {
            job.setStatus("0"); // active by default
        }

        int rows = jobMapper.insert(job);
        // After insert the id is generated, now we can register it in Quartz
        if (rows > 0 && "0".equals(job.getStatus())) {
            jobScheduler.scheduleJob(job);
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateJob(TaskJob job) {
        int rows = jobMapper.updateById(job);
        if (rows > 0) {
            TaskJob dbJob = jobMapper.selectById(job.getId());
            if (dbJob != null) {
                // If job is active, reschedule; if paused, we still update trigger but keep paused status
                jobScheduler.rescheduleJob(dbJob);
                if ("1".equals(dbJob.getStatus())) {
                    jobScheduler.pauseJob(dbJob);
                }
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int changeStatus(Long jobId, String status) {
        TaskJob job = jobMapper.selectById(jobId);
        if (job == null) {
            return 0;
        }

        job.setStatus(status);
        int rows = jobMapper.updateById(job);

        if (rows > 0) {
            if ("1".equals(status)) {
                // pause
                jobScheduler.pauseJob(job);
            } else if ("0".equals(status)) {
                // resume
                jobScheduler.resumeJob(job);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteJobByIds(List<Long> jobIds) {
        if (jobIds == null || jobIds.isEmpty()) {
            return 0;
        }
        for (Long jobId : jobIds) {
            TaskJob job = jobMapper.selectById(jobId);
            if (job != null) {
                jobScheduler.deleteJob(job);
            }
        }
        return jobMapper.deleteBatchIds(jobIds);
    }

    @Override
    public void runOnce(Long jobId) {
        TaskJob job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new IllegalArgumentException("Scheduled job not found: " + jobId);
        }
        jobScheduler.triggerOnce(job);
    }
}
