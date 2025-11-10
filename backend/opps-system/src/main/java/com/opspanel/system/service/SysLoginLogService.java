package com.opspanel.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opspanel.system.domain.SysLoginLog;
import com.opspanel.system.dto.log.LoginLogQuery;

/** Service for managing system login logs. */
public interface SysLoginLogService {
    IPage<SysLoginLog> page(LoginLogQuery query, int pageNum, int pageSize);
    void record(SysLoginLog log);
}
