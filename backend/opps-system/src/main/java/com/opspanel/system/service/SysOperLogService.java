package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.system.domain.SysOperLog;
import com.opspanel.system.dto.log.OperLogQuery;

/** Service for managing system operation logs. */
public interface SysOperLogService {
    IPage<SysOperLog> page(OperLogQuery query, int pageNum, int pageSize);
    void record(SysOperLog log);
}
