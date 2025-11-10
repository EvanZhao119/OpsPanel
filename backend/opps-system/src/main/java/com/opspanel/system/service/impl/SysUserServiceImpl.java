package com.opspanel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opspanel.system.domain.SysUser;
import com.opspanel.system.dto.user.UserCreateCmd;
import com.opspanel.system.dto.user.UserQuery;
import com.opspanel.system.dto.user.UserUpdateCmd;
import com.opspanel.system.mapper.SysUserMapper;
import com.opspanel.system.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** Business service for users. */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Override
    public IPage<SysUser> page(UserQuery q, int pageNum, int pageSize) {
        LambdaQueryWrapper<SysUser> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.hasText(q.getUsername()), SysUser::getUsername, q.getUsername());
        w.eq(q.getStatus() != null, SysUser::getStatus, q.getStatus());
        w.eq(q.getDeptId() != null, SysUser::getDeptId, q.getDeptId());
        w.orderByDesc(SysUser::getCreateTime);
        return this.page(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public Long create(UserCreateCmd cmd) {
        SysUser u = new SysUser();
        u.setUsername(cmd.getUsername());
        u.setPassword(cmd.getPassword()); // encode in upper layer if needed
        u.setNickName(cmd.getNickName());
        u.setEmail(cmd.getEmail());
        u.setPhone(cmd.getPhone());
        u.setStatus(cmd.getStatus());
        u.setDeptId(cmd.getDeptId());
        this.save(u);
        return u.getUserId();
    }

    @Override
    public boolean update(UserUpdateCmd cmd) {
        SysUser u = this.getById(cmd.getUserId());
        if (u == null) return false;
        u.setNickName(cmd.getNickName());
        u.setEmail(cmd.getEmail());
        u.setPhone(cmd.getPhone());
        u.setStatus(cmd.getStatus());
        u.setDeptId(cmd.getDeptId());
        return this.updateById(u);
    }

    @Override
    public boolean remove(Long userId) {
        return this.removeById(userId);
    }
}
