package com.opspanel.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.task.domain.TaskLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for scheduled job execution logs.
 */
@Mapper
public interface TaskLogMapper extends BaseMapper<TaskLog> {
}
