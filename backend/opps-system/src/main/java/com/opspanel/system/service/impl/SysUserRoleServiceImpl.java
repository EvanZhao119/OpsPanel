package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysUserRole;
import com.opspanel.system.mapper.SysUserRoleMapper;
import com.opspanel.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation of SysUserRoleService. */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
        implements SysUserRoleService {

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        // Remove existing relations
        this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // Batch insert new ones
        List<SysUserRole> relations = roleIds.stream()
                .map(rid -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(rid);
                    return ur;
                }).collect(Collectors.toList());
        this.saveBatch(relations);
    }

    @Override
    @Transactional
    public void removeRoles(Long userId) {
        this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
    }

    @Override
    public List<Long> getRoleIdsByUser(Long userId) {
        return this.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }
}
