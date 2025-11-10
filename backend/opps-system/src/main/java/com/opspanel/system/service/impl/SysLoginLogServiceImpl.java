package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysLoginLog;
import com.opspanel.system.dto.log.LoginLogQuery;
import com.opspanel.system.mapper.SysLoginLogMapper;
import com.opspanel.system.service.SysLoginLogService;
import org.springframework.stereotype.Service;

/** Implementation for SysLoginLogService. */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog>
        implements SysLoginLogService {

    @Override
    public IPage<SysLoginLog> page(LoginLogQuery query, int pageNum, int pageSize) {
        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysLoginLog>()
                        .like(query.getUsername() != null, SysLoginLog::getUsername, query.getUsername())
                        .eq(query.getStatus() != null, SysLoginLog::getStatus, query.getStatus())
                        .orderByDesc(SysLoginLog::getLoginTime)
        );
    }

    @Override
    public void record(SysLoginLog log) {
        this.save(log);
    }
}
