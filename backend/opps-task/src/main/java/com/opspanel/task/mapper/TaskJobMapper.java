package com.opspanel.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.task.domain.TaskJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper for scheduled job definitions.
 */
@Mapper
public interface TaskJobMapper extends BaseMapper<TaskJob> {
}
