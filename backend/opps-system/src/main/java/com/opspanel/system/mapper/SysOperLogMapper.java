package com.opspanel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opspanel.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/** Mapper for system operation logs. */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {}

