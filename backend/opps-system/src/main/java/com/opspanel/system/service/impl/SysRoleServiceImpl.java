package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysRole;
import com.opspanel.system.dto.role.RoleCreateCmd;
import com.opspanel.system.dto.role.RoleQuery;
import com.opspanel.system.dto.role.RoleUpdateCmd;
import com.opspanel.system.mapper.SysRoleMapper;
import com.opspanel.system.service.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/** Implementation of SysRoleService using MyBatis-Plus. */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public IPage<SysRole> page(RoleQuery query, int pageNum, int pageSize) {
        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysRole>()
                        .like(query.getRoleName() != null, SysRole::getRoleName, query.getRoleName())
                        .eq(query.getStatus() != null, SysRole::getStatus, query.getStatus())
                        .orderByAsc(SysRole::getRoleSort)
        );
    }

    @Override
    public Long create(RoleCreateCmd cmd) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(cmd, role);
        this.save(role);
        return role.getRoleId();
    }

    @Override
    public boolean update(RoleUpdateCmd cmd) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(cmd, role);
        return this.updateById(role);
    }

    @Override
    public boolean remove(Long id) {
        return this.removeById(id);
    }
}
