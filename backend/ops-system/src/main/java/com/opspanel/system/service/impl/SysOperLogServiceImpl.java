package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysOperLog;
import com.opspanel.system.dto.log.OperLogQuery;
import com.opspanel.system.mapper.SysOperLogMapper;
import com.opspanel.system.service.SysOperLogService;
import org.springframework.stereotype.Service;

/** Implementation for SysOperLogService. */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
        implements SysOperLogService {

    @Override
    public IPage<SysOperLog> page(OperLogQuery query, int pageNum, int pageSize) {
        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysOperLog>()
                        .like(query.getOperator() != null, SysOperLog::getOperator, query.getOperator())
                        .eq(query.getStatus() != null, SysOperLog::getStatus, query.getStatus())
                        .orderByDesc(SysOperLog::getOperTime)
        );
    }

    @Override
    public void record(SysOperLog log) {
        this.save(log);
    }
}
