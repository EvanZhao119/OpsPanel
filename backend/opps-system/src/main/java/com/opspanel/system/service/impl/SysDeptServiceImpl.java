package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysDept;
import com.opspanel.system.dto.dept.DeptCreateCmd;
import com.opspanel.system.dto.dept.DeptQuery;
import com.opspanel.system.dto.dept.DeptUpdateCmd;
import com.opspanel.system.mapper.SysDeptMapper;
import com.opspanel.system.service.SysDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of SysDeptService using MyBatis-Plus.
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public IPage<SysDept> page(DeptQuery query, int pageNum, int pageSize) {
        return this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysDept>()
                        .like(query.getDeptName() != null, SysDept::getDeptName, query.getDeptName())
                        .eq(query.getParentId() != null, SysDept::getParentId, query.getParentId())
                        .eq(query.getStatus() != null, SysDept::getStatus, query.getStatus())
                        .orderByAsc(SysDept::getOrderNum)
        );
    }

    @Override
    public Long create(DeptCreateCmd cmd) {
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(cmd, dept);
        this.save(dept);
        return dept.getDeptId();
    }

    @Override
    public boolean update(DeptUpdateCmd cmd) {
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(cmd, dept);
        return this.updateById(dept);
    }

    @Override
    public boolean remove(Long id) {
        return this.removeById(id);
    }
}
